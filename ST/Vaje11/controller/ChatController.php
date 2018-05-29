<?php

require_once("model/ChatDB.php");
require_once("ViewHelper.php");

class ChatController {

    public static function index() {
        $type = filter_input(INPUT_GET, 'type', FILTER_VALIDATE_REGEXP, [
            "options" => [
                "default" => "sse",
                "regexp" => "/^(sse)|(poll)$/"
            ]
        ]);

        if ($type == "sse") {
            ViewHelper::render("view/chat-sse.php");
        } else {
            ViewHelper::render("view/chat-poll.php");
        }
    }

    public static function getMessagesPolling() {
        header("Content-Type: application/json");
        header("Cache-Control: no-cache");

        $id = filter_input(INPUT_GET, 'id', FILTER_VALIDATE_INT, [
            "options" => [
                "default" => 0,
                "min_range" => 1
            ]
        ]);

        $messages = ChatDB::getAllSinceId($id);
        echo json_encode($messages);
    }

    public static function getMessagesAndWait() {
        header('Content-Type: text/event-stream');
        header('Cache-Control: no-cache');

        $last_id = filter_input(INPUT_SERVER, 'HTTP_LAST_EVENT_ID', FILTER_VALIDATE_INT, [
            "options" => [
                "default" => 0,
                "min_range" => 1
            ]
        ]);

        // From now on, we are "finished" with the request,
        // and have to save potential changes to session and
        // release it
        session_write_close();
        
        // disable automatic detection of client disconnect
        ignore_user_abort(true);

        $start = microtime(true);

        while (true) {
            // if the client disconnects, end the loop
            if (connection_aborted()) {
                exit();
            }
            
            // Get new messages
            $messages = ChatDB::getAllSinceId($last_id);
            foreach ($messages as $message) {
                // write them to the socket in the SSE format
                self::writeMessage($message["id"], json_encode($message));
                $last_id = $message["id"];
            } 

            # Hack to properly detect disconnected clients
            # PHP will not detect that the client has aborted the connection
            # until an attempt is made to send information to the client. 
            # Let's send a space every 10s
            if (microtime(true) - $start > 10) {
                echo PHP_EOL;
                $start = microtime(true);
            }

            // If we do not manually flush, all echo statements are buffered
            ob_flush();
            flush();

            // Sleep for 1 second
            sleep(1);
        }
    }

    public static function getMessagesAndClose() {
        header("Content-Type: text/event-stream");
        header("Cache-Control: no-cache");

        $last_id = filter_input(INPUT_SERVER, 'HTTP_LAST_EVENT_ID', FILTER_VALIDATE_INT, [
            "options" => [
                "default" => 0,
                "min_range" => 1
            ]
        ]);
        $messages = ChatDB::getAllSinceId($last_id);

        foreach ($messages as $message) {
            self::writeMessage($message["id"], json_encode($message));
        }
    }

    private static function writeMessage($id, $message, $event = "message", $retry = 3) {
        echo 'id: ' . $id . PHP_EOL;
        echo 'event: ' . $event . PHP_EOL;
        echo 'retry: ' . $retry . PHP_EOL;
        echo 'data: ' . $message . PHP_EOL;
        echo PHP_EOL;
    }

    public static function add() {
        $rules = [
            "user" => [
                "filter" => FILTER_VALIDATE_REGEXP,
                "options" => ["regexp" => "/^\w+[ ]*\w+$/"]
            ],
            "message" => FILTER_SANITIZE_SPECIAL_CHARS,
        ];
        $data = filter_input_array(INPUT_POST, $rules);

        if ($data["user"] !== false && !empty($data["message"])) {
            ChatDB::insert($data["user"], $data["message"]);
            # Return nothing
        } else {
            throw new Exception("Missing / incorrect data");
        }
    }

    public static function delete() {
        ChatDB::deleteAll();
        ViewHelper::redirect(BASE_URL . "chat");
    }
}