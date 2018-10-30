<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <style type="text/css">
        #container {
            width: 100%;
            height: auto;
        }

        .inner-container {
            min-height: 400px;
            display: inline-block;
            overflow-y: auto;
            border: 1px solid black;
        }

        #chats-container {
            height: 70%;
            width: 80%;
        }

        #users-container {
            height: 70%;
            width: 15%;
        }
    </style>
</head>
<body>
<div id="container">
    <div id="chats-container" class="inner-container">
    </div>
    <div id="users-container" class="inner-container">
    </div>
</div>
<div id="new-chat-container">
    <textarea id="new-chat-input" cols="50"></textarea>
    <br/>
    <input type="button" value="Send" id="new-chat-button"><br/><br/>
    <form method="post" action="/logout">
        <input type="submit" value="logout"/>
    </form>
</div>
</body>
<script type="text/javascript">
    function sendChat() {
        var message = document.getElementById('new-chat-input').value.trim(),
                xhr = new XMLHttpRequest();

        xhr.open('POST', encodeURI('/post-chat'));
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onload = function (response) {
            if (xhr.status === 200) {
                document.getElementById('new-chat-input').value = '';
            } else {
                alert('Request failed.  Returned status of ' + xhr.status);
                location.href = '/';
            }
        };
        xhr.send(encodeURI('message=' + message));
    }

    function fetchAllChats() {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', encodeURI('/get-all-chats'));
        xhr.onload = function (response) {
            try {
                if (xhr.status === 200) {
                    var json = JSON.parse(response.target.responseText);
                    var chats_container = document.getElementById('chats-container');
                    var chats = '';
                    for (var i = 0; i < json.length; i++) {
                        chats += '<div><strong>' + json[i].user.name + ': </strong><span>' + json[i].message + '</span>';
                    }
                    chats_container.innerHTML = chats;
                } else {
                    alert('Request failed.  Returned status of ' + xhr.status);
                }
            } catch (e) {
                console.log(e);
            }
        };
        xhr.send();
    }

    function fetchAllUsers() {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', encodeURI('/get-all-users'));
        xhr.onload = function (response) {
            try {
                if (xhr.status === 200) {
                    var json = JSON.parse(response.target.responseText);
                    var users_container = document.getElementById('users-container');
                    var users = '';
                    for (var i = 0; i < json.length; i++) {
                        users += '<strong>' + json[i].name + '</strong><br/>';
                    }
                    users_container.innerHTML = users;
                } else {
                    alert('Request failed.  Returned status of ' + xhr.status);
                }
            } catch (e) {
                console.log(e);
            }
        };
        xhr.send();
    }

    document.getElementById('new-chat-button').addEventListener('click', sendChat);
    document.getElementById('new-chat-input').addEventListener('keyup', function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if (code == 13) { // enter key press
            sendChat();
        }
    });

    setInterval(fetchAllChats, 2000);
    setInterval(fetchAllUsers, 2000);

</script>
</html>