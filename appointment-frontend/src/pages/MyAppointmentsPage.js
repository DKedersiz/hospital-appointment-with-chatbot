import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Typography,
  Grid,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@mui/material";
import { styled } from "@mui/material/styles";
import axios from "axios";

const StyledPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(4),
  margin: "auto",
  maxWidth: 800,
  marginTop: theme.spacing(8),
  textAlign: "center",
}));

const MyAppointmentsPage = () => {
  const navigate = useNavigate();
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedAppointmentId, setSelectedAppointmentId] = useState(null);

  useEffect(() => {
    axios
      .get("http://localhost:8080/rest/api/user/current", {
        withCredentials: true,
      })
      .then((response) => {
        const userEmail = response.data.email;
        axios
          .get(
            `http://localhost:8080/rest/api/appointment/getByPatientEmail?email=${userEmail}`,
            { withCredentials: true }
          )
          .then((res) => {
            console.log("Randevular:", res.data);
            // Veriyi appointments state'ine atıyoruz
            setAppointments(res.data || []); // res.data boşsa boş array atıyoruz
            setLoading(false);
          })
          .catch((err) => {
            setError("Randevular yüklenemedi: " + err.message);
            setLoading(false);
          });
      })
      .catch((err) => {
        console.error("Kullanıcı email’i alınamadı:", err);
        setError("Lütfen giriş yapın.");
        navigate("/login");
        setLoading(false);
      });
  }, [navigate]);

  const handleDeleteClick = (id) => {
    console.log("Silinecek randevu ID:", id);
    if (!id) {
      setError("Geçersiz randevu ID’si.");
      return;
    }
    setSelectedAppointmentId(id);
    setOpenDialog(true);
  };

  const handleDialogClose = () => {
    setOpenDialog(false);
    setSelectedAppointmentId(null);
  };

  const handleDeleteConfirm = () => {
    console.log("Onaylanan ID:", selectedAppointmentId);
    if (!selectedAppointmentId) {
      setError("Silinecek randevu ID'si bulunamadı.");
      setOpenDialog(false);
      return;
    }

    axios
      .delete(
        `http://localhost:8080/rest/api/appointment/delete/${selectedAppointmentId}`,
        { withCredentials: true }
      )
      .then((response) => {
        console.log("Silme başarılı:", response);
        setAppointments(
          appointments.filter((app) => app.id !== selectedAppointmentId)
        );
        setOpenDialog(false);
        setSelectedAppointmentId(null);
      })
      .catch((err) => {
        console.error("Silme hatası:", err);
        setError(
          "Randevu silme başarısız: " +
            (err.response?.data?.message || err.message)
        );
        setOpenDialog(false);
      });
  };

  if (loading) {
    return <Typography>Yükleniyor...</Typography>;
  }

  return (
    <Box sx={{ flexGrow: 1 }}>
      <StyledPaper elevation={3}>
        <Typography variant="h4" gutterBottom>
          Randevularım
        </Typography>
        {error && (
          <Typography color="error" gutterBottom>
            {error}
          </Typography>
        )}
        {appointments.length === 0 ? (
          <Typography>Henüz randevunuz bulunmamaktadır.</Typography>
        ) : (
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Doktor</TableCell>
                  <TableCell>Tarih</TableCell>
                  <TableCell>Durum</TableCell>
                  <TableCell>İşlem</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {appointments.map((appointment) => (
                  <TableRow key={appointment.id || Math.random()}>
                    {" "}
                    <TableCell>
                      {appointment.doctorName || "Bilinmiyor"}
                    </TableCell>
                    <TableCell>
                      {new Date(appointment.appointmentDate).toLocaleString()}
                    </TableCell>
                    <TableCell>{appointment.status}</TableCell>
                    <TableCell>
                      <Button
                        variant="outlined"
                        color="error"
                        onClick={() => handleDeleteClick(appointment.id)}
                      >
                        X
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </StyledPaper>

      <Dialog open={openDialog} onClose={handleDialogClose}>
        <DialogTitle>Randevu İptali</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Bu randevuyu iptal etmek istediğinizden emin misiniz?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDialogClose} color="primary">
            Hayır
          </Button>
          <Button onClick={handleDeleteConfirm} color="error" autoFocus>
            Evet
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default MyAppointmentsPage;
