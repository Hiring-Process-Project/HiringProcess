package JobAd;

import jakarta.persistence.*;

import java.time.LocalDate;

    @Entity
    @Table
    public class JobAd {
        @Id
        //Μηχανισμός που δημιουργεί αριθμητικές τιμές σε ακολουθία (sequence).
        //Αυτές οι τιμές χρησιμοποιούνται για τη γέμιση του πρωτεύοντος κλειδιού (id).
        @SequenceGenerator(
                name = "jobAd_sequence",
                sequenceName = "jobAd_sequence",
                allocationSize = 1
        )

        //Δηλώνει ότι το πεδίο id θα παίρνει αυτόματα την τιμή του από έναν generator.
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "jobAd_sequence"
        )

        private int id;
        private String title;
        private String desc;
        private LocalDate date;
        private String status;

        public JobAd(int id ,String title, String desc, LocalDate date, String status) {
            this.title = title;
            this.id = id;
            this.desc = desc;
            this.date = date;
            this.status = status;
        }

        public JobAd() {

        }

        public JobAd(String title, String desc, LocalDate date, String status) {
            this.title = title;
            this.desc = desc;
            this.date = date;
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        //Για εκτύπωση αντικειμένων σε φιλική μορφή
        @Override
        public String toString() {
            return "JobAd{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", desc='" + desc + '\'' +
                    ", date=" + date +
                    ", status='" + status + '\'' +
                    '}';
        }

    }
