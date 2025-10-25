package com.dogukankedersiz.appointment_backend.service.impl;

import com.dogukankedersiz.appointment_backend.dto.DtoDoctor;
import com.dogukankedersiz.appointment_backend.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Autowired
    private IDoctorService doctorService;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateHealthRelatedText(String userPrompt) {

        String previousContext = getPreviousContext(userPrompt);
        String availableHoursInfo = getAvailableHoursInfo();
        String doctorsBySpecialty = getDoctorsBySpecialtyFromContext(userPrompt);


        String healthContext = "Sen bir sağlık asistanısın ve yalnızca sağlıkla ilgili sorulara veya işlemlere yanıt veriyorsun. " +
                "Doktorların müsait saatlerini göstermek, randevu oluşturma, doktor seçimi veya belirli bir uzmanlık dalına göre doktor listeleme (örneğin, 'Nöroloji doktorları kim var?') gibi talepler de sağlıkla ilgili işlemlerdir ve bunlara yanıt verebilirsin. " +
                "Eğer soru sağlıkla ilgili değilse, 'Bu konuda bilgi veremem, yalnızca sağlıkla ilgili sorulara veya işlemlere yanıt verebilirim.' de. " +
                "Merhaba, selam gibi mesajlara 'Merhaba! Sana nasıl yardımcı olabilirim?' diyerek dönüş yapabilirsin. " +
                "Kullanıcı herhangi bir semptom veya hastalığını söylüyorsa (örneğin, 'Başım ağrıyor'), yapması gerekenleri ve görebileceği doktorları söyle (örneğin, Nöroloji, Dahiliye gibi). " +
                "Kullanıcı bir uzmanlık dalı sorduğunda (örneğin, 'Nöroloji doktorları kim var?'), aşağıdaki bilgileri kullanarak doktorları listele: " + doctorsBySpecialty +
                "Ayrıca, genel müsait saat bilgileri şunlardır: " + availableHoursInfo +
                "Mesajlara çok uzun cevaplar vermemeye çalış. Verilen rahatsızlık için yapması gerekenleri, rahatsızlığı oluşturabilecek sebepleri ve görebileceği uzmanlıkları söyleyebilirsin. Kişiye görebileceği uzmanlıkları söyledikten sonra randevu almak ister misiniz diye sorma çünkü senin amacın randevu vermek değil sadece kişinin rahatsızlığına çözüm bulmak.";

        String fullPrompt = (previousContext != null ? previousContext + "\n" : "") + healthContext + "\nKullanıcı sorusu: " + userPrompt;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format(
                "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}",
                fullPrompt.replace("\"", "\\\"")
        );

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=" + apiKey;

        try {
            TimeUnit.SECONDS.sleep(1);
            String response = restTemplate.postForObject(url, request, String.class);
            return extractTextFromResponse(response);
        } catch (Exception e) {
            return "Hata: " + e.getMessage();
        }
    }


    private String getPreviousContext(String userPrompt) {
        if (userPrompt.toLowerCase().contains("nöroloji doktorları") && previousSymptom != null) {
            return "Önceki soruda kullanıcı '" + previousSymptom + "' dedi ve Nöroloji önerildi.";
        }
        return null;
    }

    private String previousSymptom = null;

    private String getDoctorsBySpecialtyFromContext(String userPrompt) {
        String specialty = extractSpecialtyFromPrompt(userPrompt);
        if (specialty != null) {
            List<DtoDoctor> doctors = doctorService.getDoctorsBySpecialty(specialty);
            if (!doctors.isEmpty()) {
                StringBuilder doctorsInfo = new StringBuilder();
                for (DtoDoctor doctor : doctors) {
                    doctorsInfo.append(String.format(
                            "Dr. %s %s (%s)\n",
                            doctor.getFirstName(),
                            doctor.getLastName(),
                            doctor.getSpecialty()
                    ));
                }
                return doctorsInfo.toString();
            }
        }
        return "Belirli bir uzmanlık dalına ait doktor bulunamadı.\n";
    }


    private String extractSpecialtyFromPrompt(String userPrompt) {
        userPrompt = userPrompt.toLowerCase();
        if (userPrompt.contains("nöroloji")) return "Nöroloji";
        if (userPrompt.contains("dahiliye")) return "Dahiliye";
        if (userPrompt.contains("kardiyoloji")) return "Kardiyoloji";
        if (userPrompt.contains("kbb") || userPrompt.contains("kulak burun boğaz")) return "Kulak Burun Boğaz";
        return null;
    }


    private String getAvailableHoursInfo() {
        try {
            List<DtoDoctor> doctors = doctorService.getAllDoctors();
            StringBuilder hoursInfo = new StringBuilder();

            for (DtoDoctor doctor : doctors) {
                Long doctorId = doctor.getId();
                String doctorName = doctor.getFirstName() + " " + doctor.getLastName();
                List<String> availableHours = doctorService.getAvailableHours(doctorId);

                if (availableHours != null && !availableHours.isEmpty()) {
                    hoursInfo.append(String.format(
                            "Dr. %s (%s): %s\n",
                            doctorName,
                            doctor.getSpecialty(),
                            String.join(", ", availableHours)
                    ));
                } else {
                    hoursInfo.append(String.format(
                            "Dr. %s (%s): Şu anda müsait saati bulunmamaktadır.\n",
                            doctorName,
                            doctor.getSpecialty()
                    ));
                }
            }
            return hoursInfo.toString();
        } catch (Exception e) {
            return "Müsait saatler alınamadı: " + e.getMessage() + "\n";
        }
    }

    private String extractTextFromResponse(String response) {
        if (response != null && response.contains("\"text\": \"")) {
            int start = response.indexOf("\"text\": \"") + 9;
            int end = response.indexOf("\"", start);
            return response.substring(start, end);
        }
        return "Yanıt ayrıştırılamadı: " + response;
    }
}