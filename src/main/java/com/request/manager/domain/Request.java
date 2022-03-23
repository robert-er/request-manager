package com.request.manager.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "REQUEST")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long uniqueNumber;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Status status;

    private String details;

    @Builder
    public Request(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
