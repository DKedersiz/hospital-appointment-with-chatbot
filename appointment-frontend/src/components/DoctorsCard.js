import React from "react";
import {
  Card,
  CardActionArea,
  CardMedia,
  CardContent,
  Typography,
} from "@mui/material";

function DoctorsCard(props) {
  const { doctor } = props;

  const doctorImages = [
    "/images/doktor1.png",
    "/images/doktor2.png",
    "/images/doktor3.png",
    "/images/doktor4.png",
  ];

  const safeId = doctor.id !== undefined ? doctor.id : 0;
  const imagePath = doctorImages[safeId % doctorImages.length];

  const handleImageError = (e) => {
    console.error("Resim yüklenemedi:", e.target.src);
    e.target.src = "/images/doktor1.png";
  };

  return (
    <Card sx={{ maxWidth: 345 }}>
      <CardActionArea>
        <CardMedia
          component="img"
          height="140"
          image={imagePath}
          alt={`${doctor.firstName} ${doctor.lastName}`}
          onError={handleImageError}
        />
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {doctor.firstName} {doctor.lastName}
          </Typography>
          <Typography variant="body2" sx={{ color: "text.secondary" }}>
            {doctor.specialty || "Henüz belirtilmemiş"}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}

export default DoctorsCard;
