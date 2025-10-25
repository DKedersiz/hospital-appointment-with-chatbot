import React, { useState, useEffect } from "react";
import {
  Box,
  Typography,
  TextField,
  Button,
  Grid,
  Paper,
  MenuItem,
  Select,
  CircularProgress,
} from "@mui/material";
import { styled } from "@mui/material/styles";
import axios from "axios";

const StyledPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(4),
  margin: "auto",
  maxWidth: 600,
  marginTop: theme.spacing(8),
  textAlign: "center",
}));

const AppointmentPage = () => {
  const [formData, setFormData] = useState({
    specialty: "",
    doctorId: "",
    patientId: "",
    patientEmail: "",
    appointmentDate: "",
    appointmentTime: "",
    status: "ONAYLANDI",
  });
  const [doctors, setDoctors] = useState([]);
  const [filteredDoctors, setFilteredDoctors] = useState([]);
  const [specialties, setSpecialties] = useState([]);
  const [availableHours, setAvailableHours] = useState([]);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const initializePage = async () => {
      setLoading(true);
      try {
        const userResponse = await axios.get(
          "http://localhost:8080/rest/api/user/current",
          { withCredentials: true }
        );
        console.log("User Response:", userResponse.data);
        const userData = userResponse.data;
        const userId = userData.id;
        const userEmail = userData.email;

        if (!userId)
          throw new Error(
            "Kullanıcı ID'si bulunamadı: " + JSON.stringify(userData)
          );
        if (!userEmail)
          throw new Error(
            "Kullanıcı e-postası bulunamadı: " + JSON.stringify(userData)
          );

        const patientResponse = await axios.get(
          `http://localhost:8080/rest/api/patient/byUserId?userId=${userId}`,
          { withCredentials: true }
        );
        console.log("Patient Response:", patientResponse.data);
        const patientId = patientResponse.data.id;
        if (!patientId)
          throw new Error(
            "Hasta ID'si alınamadı: " + JSON.stringify(patientResponse.data)
          );

        setFormData((prev) => ({
          ...prev,
          patientEmail: userEmail,
          patientId,
        }));

        const doctorsResponse = await axios.get(
          "http://localhost:8080/rest/api/doctor/getAllDoctors"
        );
        console.log("Doctors Response:", doctorsResponse.data);
        if (Array.isArray(doctorsResponse.data)) {
          setDoctors(doctorsResponse.data);
          const uniqueSpecialties = [
            ...new Set(doctorsResponse.data.map((doctor) => doctor.specialty)),
          ];
          setSpecialties(uniqueSpecialties);
          setFilteredDoctors(doctorsResponse.data);
        } else {
          throw new Error(
            "Doktorlar beklenen formatta değil: " +
              JSON.stringify(doctorsResponse.data)
          );
        }
      } catch (err) {
        setError(err.message || "Bir hata oluştu: " + err.message);
        console.error("Hata:", err);
      } finally {
        setLoading(false);
      }
    };

    initializePage();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));

    if (name === "specialty") {
      const filtered = value
        ? doctors.filter((doctor) => doctor.specialty === value)
        : doctors;
      setFilteredDoctors(filtered);
      setFormData((prev) => ({ ...prev, doctorId: "", appointmentTime: "" }));
      setAvailableHours([]);
    }

    if (name === "doctorId" && value) {
      setLoading(true);
      axios
        .get(
          `http://localhost:8080/rest/api/doctor/getAvailableHours/${value}`,
          { withCredentials: true }
        )
        .then((response) => {
          console.log("Available Hours Response:", response.data);
          if (Array.isArray(response.data)) {
            setAvailableHours(response.data);
          } else {
            setError("Müsait saatler beklenen formatta değil.");
            setAvailableHours([]);
          }
        })
        .catch((err) => setError("Müsait saatler alınamadı: " + err.message))
        .finally(() => setLoading(false));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    setLoading(true);

    console.log("Form Data:", formData);
    if (
      !formData.specialty ||
      !formData.doctorId ||
      !formData.appointmentDate ||
      !formData.appointmentTime ||
      !formData.patientId
    ) {
      setError(
        "Lütfen tüm zorunlu alanları doldurun: specialty=" +
          formData.specialty +
          ", doctorId=" +
          formData.doctorId +
          ", patientId=" +
          formData.patientId
      );
      setLoading(false);
      return;
    }

    const dateTimeString = `${formData.appointmentDate}T${formData.appointmentTime}:00`;
    const appointmentDateTime = dateTimeString;

    const payload = {
      doctorId: Number(formData.doctorId),
      patientId: Number(formData.patientId),
      appointmentDate: appointmentDateTime,
      status: formData.status,
    };

    console.log("Payload:", payload);

    axios
      .post("http://localhost:8080/rest/api/appointment/create", payload, {
        withCredentials: true,
      })
      .then((response) => {
        setSuccess("Randevu başarıyla oluşturuldu!");
        setTimeout(() => setSuccess(""), 3000);
        setFormData({
          specialty: "",
          doctorId: "",
          patientId: formData.patientId,
          patientEmail: formData.patientEmail,
          appointmentDate: "",
          appointmentTime: "",
          status: "ONAYLANDI",
        });
        setAvailableHours([]);
        setFilteredDoctors(doctors);
      })
      .catch((error) => {
        setError(
          "Randevu oluşturma başarısız: " +
            (error.response?.data?.message || error.message)
        );
        console.error("Error:", error);
      })
      .finally(() => setLoading(false));
  };

  if (loading) {
    return (
      <Box
        sx={{
          flexGrow: 1,
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          minHeight: "100vh",
        }}
      >
        <CircularProgress color="primary" size={60} thickness={4} />
      </Box>
    );
  }

  const today = new Date().toISOString().split("T")[0];

  return (
    <Box sx={{ flexGrow: 1 }}>
      <StyledPaper elevation={3}>
        <Typography variant="h4" gutterBottom>
          Randevu Oluştur
        </Typography>
        {error && (
          <Typography color="error" gutterBottom>
            {error}
          </Typography>
        )}
        {success && (
          <Typography color="success.main" gutterBottom>
            {success}
          </Typography>
        )}
        <form onSubmit={handleSubmit}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <Select
                fullWidth
                name="specialty"
                value={formData.specialty}
                onChange={handleChange}
                displayEmpty
                required
              >
                <MenuItem value="" disabled>
                  Uzmanlık Alanı Seçin
                </MenuItem>
                {specialties.map((specialty) => (
                  <MenuItem key={specialty} value={specialty}>
                    {specialty}
                  </MenuItem>
                ))}
              </Select>
            </Grid>
            <Grid item xs={12}>
              <Select
                fullWidth
                name="doctorId"
                value={formData.doctorId}
                onChange={handleChange}
                displayEmpty
                required
                disabled={!formData.specialty}
              >
                <MenuItem value="" disabled>
                  Doktor Seçin
                </MenuItem>
                {Array.isArray(filteredDoctors) &&
                  filteredDoctors.map((doctor) => (
                    <MenuItem key={doctor.id} value={doctor.id}>
                      {doctor.firstName} {doctor.lastName} - {doctor.specialty}
                    </MenuItem>
                  ))}
              </Select>
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Hasta E-posta"
                name="patientEmail"
                value={formData.patientEmail || ""}
                onChange={handleChange}
                disabled
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Randevu Tarihi"
                name="appointmentDate"
                type="date"
                value={formData.appointmentDate || ""}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                inputProps={{ min: today }}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <Select
                fullWidth
                name="appointmentTime"
                value={formData.appointmentTime}
                onChange={handleChange}
                displayEmpty
                required
                disabled={availableHours.length === 0}
              >
                <MenuItem value="" disabled>
                  Saat Seçin
                </MenuItem>
                {Array.isArray(availableHours) &&
                  availableHours.map((hour) => (
                    <MenuItem key={hour} value={hour}>
                      {hour}:00
                    </MenuItem>
                  ))}
              </Select>
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Durum"
                name="status"
                value={formData.status}
                onChange={handleChange}
                disabled
                required
              />
            </Grid>
            <Grid item xs={12}>
              <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                sx={{ padding: 1.5, position: "relative" }}
                disabled={loading}
              >
                {loading ? (
                  <CircularProgress
                    size={24}
                    color="inherit"
                    sx={{
                      position: "absolute",
                      top: "50%",
                      left: "50%",
                      marginTop: "-12px",
                      marginLeft: "-12px",
                    }}
                  />
                ) : (
                  "Randevu Oluştur"
                )}
              </Button>
            </Grid>
          </Grid>
        </form>
      </StyledPaper>
    </Box>
  );
};

export default AppointmentPage;
