package com.craftinginterpreters.lox;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private final Stmt.Function deceleration;
    private final Environment closure;

    LoxFunction(Stmt.Function deceleration, Environment closure) {

        this.deceleration = deceleration;
        this.closure = closure;
    }

    @Override
    public String toString() {return "<fn "+ deceleration.name.lexeme + ">";}

    @Override
    public int arity() {
        return deceleration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        for (int i = 0; i < deceleration.params.size(); i++) {
            environment.define(deceleration.params.get(i).lexeme,
                    arguments.get(i));
        }
        try {
            interpreter.executeBlock(deceleration.body, environment);
        } catch(Return returnValue) {
            return returnValue.value;
        }
        return null;
    }
}
