package com.request.manager.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "HISTORY_ENTRY")
public class HistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="history_id")
    private History history;

    LocalDateTime date;
    Status incomingStatus;
    Status outgoingStatus;
    String details;

    @Builder
    public HistoryEntry(@NotNull History history, LocalDateTime date, Status incomingStatus,
                        Status outgoingStatus, String details) {
        this.history = history;
        this.date = date;
        this.incomingStatus = incomingStatus;
        this.outgoingStatus = outgoingStatus;
        this.details = details;
    }
}
