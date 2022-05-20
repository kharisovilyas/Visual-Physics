package com.example.visualphysics10.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
//TODO: Database written using ORM ROOM
// using annotations we declare fields, this class contains only fields and equals and hashCode

@Entity
public class LessonData {
    //key for code generate
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "speed")
    public double speed;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "sound")
    public boolean sound;
    @ColumnInfo(name = "education")
    public boolean education;
    @ColumnInfo(name = "speed2")
    public double speed2;
    @ColumnInfo(name = "acc")
    public double acc;
    @ColumnInfo(name = "radius")
    public double radius;
    @ColumnInfo(name = "mass1")
    public double mass1;
    @ColumnInfo(name = "mass2")
    public double mass2;
    @ColumnInfo(name = "strength")
    public double strength;
    @ColumnInfo(name ="angle")
    public double angle;
    @ColumnInfo(name = "elasticImpulse")
    public boolean elasticImpulse;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonData that = (LessonData) o;
        return id == that.id && Double.compare(that.speed, speed) == 0 && Double.compare(that.speed2, speed2) == 0 && Double.compare(that.acc, acc) == 0 && Double.compare(that.radius, radius) == 0 && Double.compare(that.mass1, mass1) == 0 && Double.compare(that.mass2, mass2) == 0 && Double.compare(that.strength, strength) == 0 && Double.compare(that.angle, angle) == 0 && elasticImpulse == that.elasticImpulse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, speed, speed2, acc, radius, mass1, mass2, strength, angle, elasticImpulse);
    }




}
