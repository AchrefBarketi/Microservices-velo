package com.example.velorentms.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Velo {
    public String _id;
    public String model ;
    public String Code ;
    public Integer price ;
    public String status;
    public String disponibilite ;
    public String type ;



}
