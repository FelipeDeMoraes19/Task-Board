package com.exemplo.repository;

import com.exemplo.domain.Card;
import java.util.List;

public interface CardRepository {
    Card save(Card card);
    Card findById(int id);
    List<Card> findByColumnId(int columnId);
    void update(Card card);
    void deleteById(int id);
}