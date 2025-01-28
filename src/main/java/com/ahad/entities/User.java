package com.ahad.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    private LocalDate dob; // Using LocalDate instead of Date for better type safety.

    private LocalDateTime registeredTime;

    private String gender;

    private String role = "NORMAL";

    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Quiz> quizzes; // Renamed `quizes` to `quizzes`.

    private boolean isEnable = false; // Default value for `isEnable`.

}
