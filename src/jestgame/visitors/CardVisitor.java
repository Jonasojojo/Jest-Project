package jestgame.visitors;

import jestgame.model.Card;

public interface CardVisitor {
    void visit(Card card);
}
