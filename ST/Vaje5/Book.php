<?php

class Book
{
    public $title = null;
    public $author = null;
    public $id = 0;

    /**
     * Constructor: creates a new instance.
     * @param $id
     * @param $title
     * @param $author
     * @param $price
     */
    public function __construct($id, $title, $author, $price)
    {
        $this->id = $id;
        $this->title = $title;
        $this->author = $author;
        $this->price = $price;
    }

    /**
     * A string representation
     * @return string
     */
    public function __toString()
    {
        return sprintf("Book{%d, %s, %s, %d}", $this->id, $this->author, $this->title, $this->price);
    }

}