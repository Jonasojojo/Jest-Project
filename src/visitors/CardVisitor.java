package visitors;

import model.Card;

public interface CardVisitor {
    void visit(Card card);
}
