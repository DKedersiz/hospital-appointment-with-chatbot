import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { TextField, Button, Container, Box, Alert } from "@mui/material";
import { Link } from "react-router-dom";
import Typography from "@mui/material/Typography";

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);

    try {
      const response = await fetch("http://localhost:8080/rest/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({
          username: email,
          password: password,
        }).toString(),
        credentials: "include",
      });

      if (response.status === 200 || response.status === 302) {
        const from = location.state?.from?.pathname || "/appointment";
        navigate(from, { replace: true });
        window.location.reload(); 
      } else {
        const errorText = await response.text();
        setError(
          "Giriş başarısız: " +
            (errorText || "Kullanıcı adı veya şifre yanlış.")
        );
      }
    } catch (err) {
      setError("Bir hata oluştu: " + err.message);
      console.error("Login error:", err);
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return <Typography>Yükleniyor...</Typography>;
  }

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 5, p: 3, border: "1px solid #ccc", borderRadius: "5px" }}>
        <Typography variant="h4" gutterBottom>
          Giriş Yap
        </Typography>
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}
        <form onSubmit={handleLogin}>
          <TextField
            label="E-posta"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Şifre"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            fullWidth
            margin="normal"
            required
          />
          <Box sx={{ mt: 2, display: "flex", justifyContent: "space-between" }}>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              disabled={isLoading}
            >
              Giriş Yap
            </Button>
            <Button
              component={Link}
              to="/register"
              variant="outlined"
              color="secondary"
            >
              Kayıt Ol
            </Button>
          </Box>
        </form>
      </Box>
    </Container>
  );
};

export default LoginPage;
