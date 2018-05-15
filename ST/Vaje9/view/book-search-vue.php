<!DOCTYPE html>

<link rel="stylesheet" type="text/css" href="<?= ASSETS_URL . "style.css" ?>">
<meta charset="UTF-8" />
<title>AJAX Book search</title>

<h1>Vue Book search</h1>

<p>[
<a href="<?= BASE_URL . "book" ?>">All books</a> |
<a href="<?= BASE_URL . "book/search" ?>">Search</a> | 
<a href="<?= BASE_URL . "book/add" ?>">Add new</a> |
<a href="<?= BASE_URL . "store" ?>">Bookstore</a> |
<a href="<?= BASE_URL . "store/ajax" ?>">AJAX Bookstore</a> |
<a href="<?= BASE_URL . "book/search/ajax" ?>">AJAX Search</a> |
<a href="<?= BASE_URL . "book/search/vue" ?>">Vue Search</a>
]</p>

<label>Search:
    <input id="search-field" type="text"
        name="query" autocomplete="off"
        v-on:keyup="search" autofocus /> <!-- Binds keyup event to search function (see below) -->
</label>

<p>This won't work untill you implement Step 1 of Assignment 2.</p>

<div id="hits">
  <ol>
    <!-- Vue template for displaying a list of books -->
    <li v-for="book in books">
      <a :href="'<?= BASE_URL . "book?id=" ?>' + book.id">{{ book.author }}: {{ book.title }}</a>
    </li>
  </ol>
</div>

<script src="https://unpkg.com/vue"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script>
const hits = new Vue({
    el: '#hits', // Vue app will live in the context of the #hits element
    data: { // contains vue App data
        books: [] // intitially the list of books is empty
    }
});

const search = new Vue({
    el: '#search-field',
    methods: {
        search(event) { // make a search request
            const query = event.target.value;
            if (query === "") { // abort if parameter is empty
                hits.books = [];
                return
            }
            // Axios is library for making HTTP requests from browser (and node.js).
            // It is an alternative to jQuery's $.ajax
            axios.get(
                "<?= BASE_URL . "api/book/search/" ?>",
                { params: { query } }
            // handle successful response
            // all we have to do is to set received data into our books variable, vue will
            // render elements as specified in the template above
            ).then(response => hits.books = response.data 
            // handle error
            ).catch(error => console.log(error))
        }
    }
})
</script>