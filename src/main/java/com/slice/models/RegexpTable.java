package com.slice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "regexpholder", catalog = "pii")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegexpTable {

    @Id
    private int id;

    @Column
    private String regexpTest;

    @Column
    private String replaceWithTest;

}
