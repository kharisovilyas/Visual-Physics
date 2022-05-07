package com.example.visualphysics10.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class LessonData {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public double getSpeed() {
        return speed;
    }

    @ColumnInfo(name = "speed")
    public double speed;
    @ColumnInfo(name = "speed2")
    public double speed2;
    @ColumnInfo(name = "distance")
    public double distance;
    @ColumnInfo(name = "time")
    public double time;
    @ColumnInfo(name = "acc")
    public double acc;
    @ColumnInfo(name = "radius")
    public double radius;
    @ColumnInfo(name = "mass1")
    public double mass1;
    @ColumnInfo(name = "mass2")
    public double mass2;
    @ColumnInfo(name = "x")
    public double x;
    @ColumnInfo(name = "y")
    public double y;
    @ColumnInfo(name = "strength")
    public double strength;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonData that = (LessonData) o;
        return id == that.id && Double.compare(that.speed, speed) == 0 && Double.compare(that.speed2, speed2) == 0 && Double.compare(that.distance, distance) == 0 && Double.compare(that.time, time) == 0 && Double.compare(that.acc, acc) == 0 && Double.compare(that.radius, radius) == 0 && Double.compare(that.mass1, mass1) == 0 && Double.compare(that.mass2, mass2) == 0 && Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.strength, strength) == 0 && Double.compare(that.angle, angle) == 0 && elasticImpulse == that.elasticImpulse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, speed, speed2, distance, time, acc, radius, mass1, mass2, x, y, strength, angle, elasticImpulse);
    }

    @ColumnInfo(name ="angle")
    public double angle;
    @ColumnInfo(name = "elasticImpulse")
    public boolean elasticImpulse;


}
