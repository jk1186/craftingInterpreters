package com.craftinginterpreters.lox;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {

    final Environment enclosing;
    private final List<String> uninitializedVariables = new ArrayList<>();
    private final Map<String, Object> values = new HashMap<>();

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        if(values.containsKey(name.lexeme) || uninitializedVariables.contains(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if(enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void define(String name, Object value) {
        if (value != null) {
            values.put(name, value);
        } else {
            uninitializedVariables.add(name);
        }
    }

    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 9; i < distance; i++) {
            environment = environment.enclosing;
        }

        return environment;
    }

    Object getAt(int distance, String name) {
        return ancestor(distance).values.get(name);
    }

    void assignAt(int distance, Token name, Object value) {
        ancestor(distance).values.put(name.lexeme, value);
    }
}
