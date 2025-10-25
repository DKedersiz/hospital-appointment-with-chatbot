import React, { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Header from "./components/Header";
import LoginPage from "./pages/LoginPage";
import AppointmentPage from "./pages/AppointmentPage";
import MyAppointmentsPage from "./pages/MyAppointmentsPage";
import RegisterPage from "./pages/RegisterPage";
import HomePage from "./pages/HomePage";
import DoctorsPage from "./pages/DoctorsPage";
import CommunicationPage from "./pages/CommunicationPage";
import axios from "axios";
import Chatbot from "./components/chatbot";
import Typography from "@mui/material/Typography";

const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/rest/api/user/current",
          { withCredentials: true }
        );
        setIsAuthenticated(response.status === 200);
      } catch (err) {
        setIsAuthenticated(false);
        console.error("Oturum kontrolü hatası:", err);
      } finally {
        setLoading(false);
      }
    };

    checkAuth();
  }, []);

  if (loading) {
    return <Typography>Yükleniyor...</Typography>;
  }

  return (
    <Router>
      <Header
        isAuthenticated={isAuthenticated}
        setIsAuthenticated={setIsAuthenticated}
      />
      <Chatbot />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route
          path="/login"
          element={
            !isAuthenticated ? (
              <LoginPage />
            ) : (
              <Navigate to="/appointment" replace />
            )
          }
        />
        <Route
          path="/appointment"
          element={
            isAuthenticated ? (
              <AppointmentPage />
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />
        <Route
          path="/my-appointments"
          element={
            isAuthenticated ? (
              <MyAppointmentsPage />
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/doctors" element={<DoctorsPage />} />
        <Route path="/contact" element={<CommunicationPage />} />
      </Routes>
    </Router>
  );
};

export default App;
