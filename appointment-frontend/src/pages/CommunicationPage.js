import React, { useState } from "react";
import { Box, Typography, TextField, Button, Grid, Paper } from "@mui/material";
import { styled } from "@mui/material/styles";
import LocalPhoneIcon from "@mui/icons-material/LocalPhone";
import EmailIcon from "@mui/icons-material/Email";
import RoomIcon from "@mui/icons-material/Room";

const StyledPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(4),
  margin: "auto",
  maxWidth: 600,
  marginTop: theme.spacing(8),
  textAlign: "center",
}));

const CommunicationPage = () => {
  const contactInfo = {
    email: "example.random@domain.com",
    phone: "+90 555 123 4567",
    address:
      "Akademi Mahallesi Yeni İstanbul Caddesi No:313 Selçuk Üniversitesi Alaeddin Keykubat Yerleşkesi Selçuklu/Konya 42130",
  };

  const [successMessage, setSuccessMessage] = useState("");
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    message: "",
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log("Mesaj gönderildi:", formData);
    setSuccessMessage("Mesajınız başarıyla gönderildi!");
    setFormData({ name: "", email: "", message: "" });
    setTimeout(() => setSuccessMessage(""), 3000);
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <StyledPaper elevation={3}>
        <Typography variant="h4" gutterBottom>
          Bize Ulaşın
        </Typography>
        <Typography variant="body1" gutterBottom>
          Sorularınız veya önerileriniz için bizimle iletişime geçebilirsiniz.
        </Typography>

        {/* Başarı Mesajı */}
        {successMessage && (
          <Typography
            color="success.main"
            sx={{
              mb: 2,
              padding: 1,
              backgroundColor: "#e0f7fa",
              borderRadius: 1,
            }}
          >
            {successMessage}
          </Typography>
        )}

        {/* İletişim Bilgileri */}
        <Grid container spacing={2} sx={{ marginBottom: 4 }}>
          <Grid item xs={12} sm={6}>
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <EmailIcon sx={{ marginRight: 1 }} />
              <Typography variant="body2">{contactInfo.email}</Typography>
            </Box>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <LocalPhoneIcon sx={{ marginRight: 1 }} />
              <Typography variant="body2">{contactInfo.phone}</Typography>
            </Box>
          </Grid>
          <Grid item xs={12} sm={12}>
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <RoomIcon sx={{ marginRight: 1 }} />
              <Typography variant="body2">{contactInfo.address}</Typography>
            </Box>
          </Grid>
        </Grid>

        {/* İletişim Formu */}
        <form onSubmit={handleSubmit}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Adınız"
                name="name"
                value={formData.name}
                onChange={handleChange}
                variant="outlined"
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="E-posta"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                variant="outlined"
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Mesajınız"
                name="message"
                value={formData.message}
                onChange={handleChange}
                multiline
                rows={4}
                variant="outlined"
                required
              />
            </Grid>
            <Grid item xs={12}>
              <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                sx={{ padding: 1.5 }}
              >
                Gönder
              </Button>
            </Grid>
          </Grid>
        </form>
      </StyledPaper>
    </Box>
  );
};

export default CommunicationPage;
