package com.dogukankedersiz.appointment_backend.controller.impl;

import com.dogukankedersiz.appointment_backend.entities.Doctor;
import com.dogukankedersiz.appointment_backend.service.impl.GeminiService;
import com.dogukankedersiz.appointment_backend.service.IDoctorService;
import com.dogukankedersiz.appointment_backend.service.impl.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/rest/api/processInput")
public class ChatbotController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private IDoctorService doctorService;

    @PostMapping
    public String processInput(@RequestBody String input) {

        if (input.toLowerCase().contains("müsait saat")) {
            Pattern pattern = Pattern.compile("Dr\\.\\s*(\\w+)\\s*(\\w+)");
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                String firstName = matcher.group(1);
                String lastName = matcher.group(2);
                try {

                    Doctor doctor = doctorService.getDoctorById(
                            doctorService.getDoctorById(
                                    doctorService.getAllDoctors().stream()
                                            .filter(d -> d.getFirstName().equalsIgnoreCase(firstName) && d.getLastName().equalsIgnoreCase(lastName))
                                            .findFirst()
                                            .orElseThrow(() -> new RuntimeException("Doktor bulunamadı: " + firstName + " " + lastName))
                                            .getId()
                            ).getId()
                    ).toDoctor();
                    List<String> availableHours = doctorService.getAvailableHours(doctor.getId());
                    if (availableHours.isEmpty()) {
                        return "Dr. " + firstName + " " + lastName + " şu anda müsait değil.";
                    }
                    return "Dr. " + firstName + " " + lastName + " için müsait saatler: " + String.join(", ", availableHours);
                } catch (Exception e) {
                    return "Doktor bulunamadı: " + e.getMessage();
                }
            } else {
                return "Lütfen doktor adını şu formatta belirtin: 'Dr. İsim Soyisim'";
            }
        }


        return geminiService.generateHealthRelatedText(input);
    }
}