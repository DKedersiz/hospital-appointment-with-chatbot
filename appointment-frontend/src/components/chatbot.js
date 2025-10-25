import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Box, Button } from "@mui/material";
import "./Chatbot.css";

const Chatbot = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([
    {
      text:
        "Merhaba! Ben Yardımcı Asistan. Sana nasıl yardımcı olabilirim?\n" +
        "Şunları yapabilirim:\n" +
        "1. Doktorların müsait saatlerini göster\n" +
        "2. Şikayetine göre sağlık bilgisi ver",
      sender: "bot",
      buttons: [
        { label: "Randevu Al", path: "/appointment" },
        { label: "İletişim", path: "/contact" },
      ],
    },
  ]);
  const [input, setInput] = useState("");
  const navigate = useNavigate();

  const formatMessage = (rawMessage, buttons) => {
    const lines = rawMessage.split("\n");
    const elements = [];

    lines.forEach((line, index) => {
      line = line.trim();

      if (line === "") {
        elements.push(<br key={index} />);
        return;
      }

      if (line.startsWith("**") && line.endsWith("**")) {
        elements.push(
          <p key={index} style={{ fontWeight: "bold", margin: "5px 0" }}>
            {line.replace(/\*\*/g, "")}
          </p>
        );
        return;
      }

      const listMatch = line.match(/^\d+\.\s*(.*)/) || line.match(/^\*\s*(.*)/);
      if (listMatch) {
        const listItem = listMatch[1];
        const boldMatch = listItem.match(/\*\*(.*?)\*\*/);

        let content = listItem;
        if (boldMatch) {
          const boldText = boldMatch[1];
          content = (
            <>
              <strong>{boldText}</strong>
              {listItem.replace(/\*\*(.*?)\*\*/, "").trim()}
            </>
          );
        }

        elements.push(
          <ul key={`ul-${index}`} className="chatbot-list">
            <li key={index} style={{ marginBottom: "8px" }}>
              {content}
            </li>
          </ul>
        );
        return;
      }

      elements.push(
        <p key={index} style={{ margin: "5px 0" }}>
          {line}
        </p>
      );
    });

    if (buttons && buttons.length > 0) {
      elements.push(
        <Box key="buttons" sx={{ display: "flex", gap: 1, mt: 1 }}>
          {buttons.map((button, idx) => (
            <Button
              key={idx}
              variant="contained"
              color="primary"
              size="small"
              onClick={() => navigate(button.path)}
            >
              {button.label}
            </Button>
          ))}
        </Box>
      );
    }

    return elements;
  };

  const sendMessage = async () => {
    if (!input.trim()) return;

    const userMessage = { text: input, sender: "user" };
    setMessages([...messages, userMessage]);
    setInput("");

    try {
      const payload = { userInput: input };
      console.log("Gönderilen payload:", payload);

      const response = await axios.post(
        "http://localhost:8080/rest/api/processInput",
        payload
      );
      const botResponse = response.data;
      console.log("Gelen yanıt:", botResponse);
      setMessages((prev) => [...prev, { text: botResponse, sender: "bot" }]);
    } catch (error) {
      console.error(
        "Backend Hatası:",
        error.response ? error.response.data : error.message
      );
      setMessages((prev) => [
        ...prev,
        {
          text:
            "Üzgünüm, bir hata oluştu. Lütfen tekrar deneyin. Hata: " +
            (error.response ? error.response.data : error.message),
          sender: "bot",
        },
      ]);
    }
  };

  return (
    <div className="chatbot-container">
      {!isOpen && (
        <button className="chatbot-toggle" onClick={() => setIsOpen(true)}>
          💬
        </button>
      )}
      {isOpen && (
        <div className="chatbot-window">
          <div className="chatbot-header">
            <h3>Yardımcı Asistan</h3>
            <button onClick={() => setIsOpen(false)}>X</button>
          </div>
          <div className="chatbot-messages">
            {messages.map((msg, index) => (
              <div key={index} className={`message ${msg.sender}`}>
                {formatMessage(msg.text, msg.buttons)}
              </div>
            ))}
          </div>
          <div className="chatbot-input">
            <input
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyPress={(e) => e.key === "Enter" && sendMessage()}
              placeholder="Sorunuzu yazın..."
            />
            <button onClick={sendMessage}>Gönder</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Chatbot;
