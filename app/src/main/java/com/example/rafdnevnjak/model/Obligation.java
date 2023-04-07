package com.example.rafdnevnjak.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Obligation implements Parcelable{

    protected Obligation(Parcel in) {
        title = in.readString();
        startHour = in.readInt();
        startMinute = in.readInt();
        endHour = in.readInt();
        endMinute = in.readInt();
        description = in.readString();
        obligationSeverity = ObligationSeverity.getEnum(in.readInt());
    }

    public static final Creator<Obligation> CREATOR = new Creator<Obligation>() {
        @Override
        public Obligation createFromParcel(Parcel in) {
            return new Obligation(in);
        }

        @Override
        public Obligation[] newArray(int size) {
            return new Obligation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeString(title);
        parcel.writeInt(startHour);
        parcel.writeInt(startMinute);
        parcel.writeInt(endHour);
        parcel.writeInt(endMinute);
        parcel.writeString(description);
        parcel.writeInt(obligationSeverity.getValue());
    }

    public enum ObligationSeverity {
        LOW(0),
        MID(1),
        HIGH(2);

        private int value;

        ObligationSeverity(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ObligationSeverity getEnum(int value){
            for (ObligationSeverity severity : values()) {
                if (severity.value == value) {
                    return severity;
                }
            }
            return null;
        }
    }
    private String title;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String description;

    private ObligationSeverity obligationSeverity;

    public Obligation(String title, int startHour, int startMinute, int endHour, int endMinute, String description, ObligationSeverity obligationSeverity){
        this.title = title;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.description = description;
        this.obligationSeverity = obligationSeverity;
    }

    public String getTitle() {
        return title;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public String getDescription() {
        return description;
    }

    public ObligationSeverity getObligationSeverity() {
        return obligationSeverity;
    }
}
