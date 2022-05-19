package com.example.visualphysics10.database;

import java.io.Serializable;

public abstract class PhysicsData implements Serializable {
    //TODO: Helper class for database contains only fields and getters setters
    // its use helps us not to get confused in the logic of our physics engine and ect
    public static double radius;
    public static double speed;
    public static double speedEnd;
    public static double speed2;
    public static double distance;
    public static double acc;
    public static double mass1;
    public static double mass2;
    public static double x0;
    public static double y0;
    public boolean impact;
    public static boolean elasticImpulse;
    public static double frequency;
    public static double turns;
    public static double angle;
    public static double force;
    public static double height;
    public static double time;

    public static double getSpeedEnd() {
        return speedEnd;
    }

    public static void setSpeedEnd(double speedEnd) {
        PhysicsData.speedEnd = speedEnd;
    }

    public static double getHeight() {
        return height;
    }

    public static void setHeight(double height) {
        PhysicsData.height = height;
    }


    public static double getTime() {
        return time;
    }

    public static void setTime(double time) {
        PhysicsData.time = time;
    }


    public static double getAngle() {
        return angle;
    }

    public static void setAngle(double angle) {
        PhysicsData.angle = angle;
    }

    public static double getForce() {
        return force;
    }

    public static void setForce(double force) {
        PhysicsData.force = force;
    }

    public static double getFrequency() {
        return frequency;
    }

    public static void setFrequency(double frequency) {
        PhysicsData.frequency = frequency;
    }

    public static double getTurns() {
        return turns;
    }

    public static void setTurns(double turns) {
        PhysicsData.turns = turns;
    }


    public static boolean getElasticImpulse() {
        return elasticImpulse;
    }

    public static void setElasticImpulse(boolean elasticImpulse) {
        PhysicsData.elasticImpulse = elasticImpulse;
    }

    public static double getSpeed2() {
        return speed2;
    }

    public static void setSpeed2(double speed2) {
        PhysicsData.speed2 = speed2;
    }

    public boolean isImpact() {
        return impact;
    }

    public void setImpact(boolean impact) {
        this.impact = impact;
    }

    public static double getX0() {
        return x0;
    }

    public static void setX0(double x0) {
        PhysicsData.x0 = x0;
    }

    public static double getY0() {
        return y0;
    }

    public static void setY0(double y0) {
        PhysicsData.y0 = y0;
    }

    public static double getRadius() {
        return radius;
    }

    public static void setRadius(double radius) {
        PhysicsData.radius = radius;
    }

    public static double getSpeed() {
        return speed;
    }

    public static void setSpeed(double speed) {
        PhysicsData.speed = speed;
    }

    public static double getDistance() {
        return distance;
    }

    public static void setDistance(double distance) {
        PhysicsData.distance = distance;
    }

    public static double getAcc() {
        return acc;
    }

    public static void setAcc(double acc) {
        PhysicsData.acc = acc;
    }

    public static double getMass1() {
        return mass1;
    }

    public static void setMass1(double mass1) {
        PhysicsData.mass1 = mass1;
    }

    public static double getMass2() {
        return mass2;
    }

    public static void setMass2(double mass2) {
        PhysicsData.mass2 = mass2;
    }

}
