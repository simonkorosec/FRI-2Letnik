<?php

require_once 'Book.php';

/*
 * BookDB simulates a database of books. 
 *
 * Usually, this kind of data would be fetched from a database. But
 * for this exercise, we will use a set of hard-coded books provided 
 * in a associative array.
 */

class BookDB
{

    /**
     * Returns the list of all books from the 'database'
     *
     * @return array - Associative array of all books.
     */
    public static function getAllBooks()
    {
        $books = array();
        $books[1] = new Book(1, "Prolog Programming for Artificial Intelligence", "Ivan Bratko", 43);
        $books[2] = new Book(2, "Arhitektura računalniških sistemov", "Dušan Kodek", 25);
        $books[3] = new Book(3, "Managing Information Systems Security and Privacy", "Denis Trček", 36);
        $books[4] = new Book(4, "Študijski koledar", "FRI", 5);

        return $books;
    }

    /**
     * Returns a book with a given ID. If no such book exists, an exception is thrown.
     * @param  $id
     * @return Book
     */
    public static function get($id)
    {
        $allBooks = self::getAllBooks();

        if (isset($allBooks[$id])) {
            return $allBooks[$id];
        } else {
            throw new InvalidArgumentException("There is no book with id = $id.");
        }
    }

    public static function find($query)
    {
        if ($query == ""){
            return self::getAllBooks();
        }

        $books = [];
        foreach (BookDB::getAllBooks() as $book):
            $title = $book->title;
            if (mb_stripos($title, $query) !== false){
                array_push($books, $book);
            }
        endforeach;

        return $books;
    }
}