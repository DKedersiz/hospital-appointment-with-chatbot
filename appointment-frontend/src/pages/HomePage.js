import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Typography,
  Grid,
  Card,
  CardContent,
  Button,
  Box,
  colors,
} from "@mui/material";
import {
  Favorite,
  Visibility,
  Healing,
  Hearing,
  FavoriteBorder,
} from "@mui/icons-material";
import GavelIcon from "@mui/icons-material/Gavel";
import axios from "axios";

function HomePage() {
  const navigate = useNavigate();
  const [doctors, setDoctors] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    axios
      .get("http://localhost:8080/rest/api/doctor/getAllDoctors")
      .then((response) => {
        const validDoctors = response.data.filter(
          (doctor) => doctor.id && doctor.firstName && doctor.lastName
        );
        setDoctors(validDoctors.slice(0, 3));
      })
      .catch((err) => setError("Doktorlar yüklenemedi: " + err.message));
  }, []);

  const services = [
    {
      title: "Kardiyoloji",
      description:
        "Kalp sağlığınız için uzman kadromuzla yanınızdayız. Gelişmiş teşhis ve tedavi yöntemleriyle kalbinizi koruyoruz.",
      icon: <Favorite />,
    },
    {
      title: "Göz Hastalıkları",
      description:
        "Göz sağlığınız için en modern teknolojilerle hizmet veriyoruz. Katarakt, glokom ve retina hastalıkları gibi sorunlarda uzman ekibimizle yanınızdayız.",
      icon: <Visibility />,
    },
    {
      title: "Üroloji",
      description:
        "Ürolojik sağlık sorunlarınızda güvenilir çözümler sunuyoruz. Böbrek taşı, prostat ve idrar yolu enfeksiyonları gibi konularda uzman kadromuzla destek veriyoruz.",
      icon: <Healing />,
    },
    {
      title: "Kulak Burun Boğaz (KBB)",
      description:
        "Kulak, burun ve boğaz sağlığınız için kapsamlı teşhis ve tedavi hizmetleri sunuyoruz. İşitme kaybı, sinüzit ve alerji problemlerinde uzmanlarımızla yanınızdayız.",
      icon: <Hearing />,
    },
    {
      title: "Kalp ve Damar Cerrahisi",
      description:
        "Kalp ve damar sağlığınız için ileri cerrahi yöntemlerle hizmet veriyoruz. Bypass ameliyatları ve damar tıkanıklığı tedavilerinde uzman ekibimizle güvenle yanınızdayız.",
      icon: <FavoriteBorder />,
    },
  ];

  return (
    <div>
      <div
        style={{
          background:
            'linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url("/images/hastane5.png")',
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
          backgroundSize: "cover",
          height: "500px",
          minHeight: "100vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Typography
          variant="h1"
          color="#00D4FF"
          sx={{ textShadow: "2px 2px 4px rgba(0,0,0,0.7)" }}
        >
          Selçuk Üni. Hastanesi'ne Hoş Geldiniz!
        </Typography>
      </div>

      <Box sx={{ py: 5, px: 2 }}>
        <Typography variant="h4" align="center" gutterBottom>
          Vizyonumuz ve Misyonumuz
        </Typography>
        <Grid container spacing={3} sx={{ mt: 2 }}>
          <Grid item xs={12} md={6}>
            <Card sx={{ minHeight: "200px", boxShadow: 3 }}>
              <CardContent>
                <Typography variant="h5" gutterBottom align="center">
                  <GavelIcon />
                  Vizyonumuz
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  Sağlıkta öncü bir kurum olarak, en yüksek standartlarda hizmet
                  sunmak ve hasta memnuniyetini her zaman ön planda tutmak. Bu
                  hedefi gerçekleştirecek, teknik ve fiziki alt yapı ile insan
                  gücüne sahip olan hastanemiz dünya tıbbındaki her türlü
                  gelişme ve yenlilikleri takip ederek kendisini yenilemekte ve
                  buna paralel olarak uluslararası açılımları hedeflemektedir.
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} md={6}>
            <Card sx={{ minHeight: "200px", boxShadow: 3 }}>
              <CardContent>
                <Typography variant="h5" gutterBottom align="center">
                  <GavelIcon />
                  Misyonumuz
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  Toplumun her kesimine kaliteli, erişilebilir ve güvenilir
                  sağlık hizmetleri sunarak sağlıklı bir yaşamı desteklemek.
                  Güler yüzlü insani değerleri her şeyin üstünde tutan, bilimsel
                  donanımlı personelimizle sağlığınızı ve sağlıklı geleceğinizi
                  emin ellere teslim almayı hedeflemekteyiz.
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Box>

      <Box sx={{ py: 5, px: 2, backgroundColor: "#f5f5f5" }}>
        <Typography variant="h4" align="center" gutterBottom>
          Doktorlarımız
        </Typography>
        {error && (
          <Typography color="error" align="center" gutterBottom>
            {error}
          </Typography>
        )}
        <Grid container spacing={4} sx={{ mt: 2, justifyContent: "center" }}>
          {doctors.length > 0 ? (
            doctors.map((doctor, index) => (
              <Grid item xs={12} sm={6} md={3} key={index}>
                <Card
                  sx={{
                    minHeight: "250px",
                    boxShadow: 3,
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "space-between",
                    borderRadius: "8px",
                  }}
                >
                  <CardContent
                    sx={{
                      flexGrow: 1,
                      display: "flex",
                      flexDirection: "column",
                      alignItems: "center",
                      textAlign: "center",
                    }}
                  >
                    <Typography
                      variant="h6"
                      gutterBottom
                      sx={{ mt: 1, fontWeight: "bold" }}
                    >
                      {doctor.firstName} {doctor.lastName}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {doctor.specialty || "Uzmanlık Alanı Belirtilmemiş"}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))
          ) : (
            <Typography align="center">Doktorlar yükleniyor...</Typography>
          )}
        </Grid>
        <Box sx={{ py: 3, textAlign: "center" }}>
          <Button
            variant="outlined"
            onClick={() => navigate("/doctors")}
            sx={{ mt: 2 }}
          >
            Tüm Doktorları Gör
          </Button>
        </Box>
      </Box>

      <Box sx={{ py: 5, px: 2 }}>
        <Typography variant="h4" align="center" gutterBottom>
          Hizmetlerimiz
        </Typography>
        <Grid container spacing={4} sx={{ mt: 2, justifyContent: "center" }}>
          {services.map((service, index) => (
            <Grid item xs={12} sm={6} md={3} key={index}>
              <Card
                sx={{
                  minHeight: "350px",
                  boxShadow: 3,
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "space-between",
                  borderRadius: "8px",
                }}
              >
                <CardContent
                  sx={{
                    flexGrow: 1,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    textAlign: "center",
                  }}
                >
                  {service.icon}
                  <Typography
                    variant="h6"
                    gutterBottom
                    sx={{ mt: 1, fontWeight: "bold" }}
                  >
                    {service.title}
                  </Typography>
                  <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{ flexGrow: 1, padding: "0 10px" }}
                  >
                    {service.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      <Box sx={{ py: 5, textAlign: "center" }}>
        <Button
          variant="contained"
          size="large"
          onClick={() => navigate("/appointment")}
          sx={{
            backgroundColor: "#1976d2",
            "&:hover": { backgroundColor: "#115293" },
          }}
        >
          Hemen Randevu Al
        </Button>
      </Box>
    </div>
  );
}

export default HomePage;
