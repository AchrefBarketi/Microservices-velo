package com.example.velorentms.Entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "velorent")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Velorent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "velo_id")
    private Long veloId;

    @Column(name = "user_id")
    private Long userId;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_rent_date")
    private Date startRentDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_rent_date")
    private Date endRentDate;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;


}