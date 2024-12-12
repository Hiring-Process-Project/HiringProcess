package com.example.hiringProcess.Interview;

import jakarta.persistence.*;

@Entity
@Table
public class Interview {
        @Id
        //Μηχανισμός που δημιουργεί αριθμητικές τιμές σε ακολουθία (sequence).
        //Αυτές οι τιμές χρησιμοποιούνται για τη γέμιση του πρωτεύοντος κλειδιού (id).
        @SequenceGenerator(
                name = "interview_sequence",
                sequenceName = "interview_sequence",
                allocationSize = 1

        )
        //Δηλώνει ότι το πεδίο id θα παίρνει αυτόματα την τιμή του από έναν generator.
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "interview_sequence"
        )

        private int id;
    public Interview(int id ) {
        this.id = id;
        // this.uploadedFiles = getUploadedFiles();
    }

    public Interview() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                '}';
    }
}
