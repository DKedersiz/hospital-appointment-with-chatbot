import React from "react";
import { useEffect, useState } from "react";
import axios from "axios";
import { Grid, Typography } from "@mui/material";
import DoctorsCard from "../components/DoctorsCard";

function DoctorsPage() {
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchDoctors = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/rest/api/doctor/getAllDoctors"
        );
        const validDoctors = response.data.filter(
          (doctor) => doctor.id && doctor.firstName && doctor.lastName
        );
        setDoctors(validDoctors);
        setLoading(false);
      } catch (err) {
        setError("Doktorlar yüklenemedi. Lütfen tekrar deneyin.");
        setLoading(false);
        console.error("Hata:", err);
      }
    };

    fetchDoctors();
  }, []);

  return (
    <div style={{ padding: "20px" }}>
      <Typography variant="h4" gutterBottom align="center">
        Doktorlar
      </Typography>
      {loading && <Typography variant="body1">Yükleniyor...</Typography>}
      {error && (
        <Typography variant="body1" color="error">
          {error}
        </Typography>
      )}
      {!loading && !error && (
        <Grid container spacing={3}>
          {doctors.map((doctor) => (
            <Grid item xs={12} sm={6} md={4} key={doctor.id}>
              <DoctorsCard doctor={doctor} />
            </Grid>
          ))}
        </Grid>
      )}
    </div>
  );
}

export default DoctorsPage;
