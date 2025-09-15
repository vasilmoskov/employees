package com.example.model;

public record Pair(Long emp1Id, Long emp2Id) {
    public static Pair of(long id1, Long id2) {
        return id1 < id2 ? new Pair(id1, id2) : new Pair(id2, id1);
    }
}
