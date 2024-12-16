package io.arcmai.mail.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "AM_OPTION")
@Entity
public class Option {
        @Id
        @Column(name = "AM_ID")
        private String id;
        @Column(name = "AM_VALUE")
        private String value;

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getValue() {
                return value;
        }

        public Integer getValueAsInteger(){
                return Integer.parseInt(value);
        }

        public Long getValueAsLong(){
                return Long.parseLong(value);
        }

        public void setValue(String value) {
                this.value = value;
        }
}
