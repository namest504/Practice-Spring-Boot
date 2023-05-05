package com.list.multipartfile.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "FileData")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private String filePath;

}
