package com.request.manager.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "HISTORY")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @OneToMany(targetEntity = HistoryEntry.class,
            mappedBy = "history",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<HistoryEntry> entries = new ArrayList<>();

    @Builder
    public History(Request request) {
        this.request = request;
    }
}
