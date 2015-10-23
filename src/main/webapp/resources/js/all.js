/**
 * Created by ysidorov on 23.10.15.
 */



var ws;


window.onload = function() {
    document.body.onselectstart = function() { return false; };
    document.body.onmousedown = function() { return false; };
    if (window.addEventListener) {
        window.addEventListener("resize", onresize);
    }
    addCardsFaces();

    // click handler for horn targets
    var targets = document.querySelectorAll('.your .row-horn');
    toArray(targets).forEach(
        function(hornTarget) {
            //TODO: allow switching between singleclick and doubleclick
            // hornTarget.addEventListener("dblclick", clickOnHornTarget);
            hornTarget.addEventListener("click", clickOnHornTarget);
        }
    );

    connect();
};
document.onmouseover = function(event) {
    var element = event.target;
    // set description
    if (element.hasAttribute("description")) {
        document.getElementById("description-text").innerText = element.getAttribute("description");
    } else {
        document.getElementById("description-text").innerText = "";
    }
    // set card image
    // http://stackoverflow.com/questions/14013131/javascript-get-background-image-url-of-div
    if (element.classList.contains("card")) {
        var card = element;
        var style = card.currentStyle || window.getComputedStyle(card, false),
            bg_image_url = style.backgroundImage.replace(/url\((['"])?(.*?)\1\)/gi, '$2').split(',')[0];
        var urlString = 'url(' + bg_image_url + ')';
        document.getElementById('big-card').style.backgroundImage =  urlString;
        highlightRow(card.getAttribute('row'));
    } else {
        document.getElementById('big-card').style.backgroundImage =  "";
        highlightRow(false);
    }
};
var highlightedRow;
function highlightRow(row) {
    /*
     if (row) {
     var rowId;
     switch (row) {
     case 'CLOSE': rowId = '#close-combat';
     break;
     case 'RANGED': rowId = '#ranged-combat';
     break;
     case 'SIEGE': rowId = '#siege-combat';
     break;
     }
     highlightedRow = document.querySelector(rowId);
     if (highlightedRow)
     highlightedRow.classList.toggle('highlighted-target');
     }
     */
}

function toArray(nodeList) {
    for (var a = [], l = nodeList.length; l--; a[l] = nodeList[l]);
    return a;
}

function toggleHighlightHornTargets () {
    // https://developer.mozilla.org/ru/docs/Web/API/NodeList
    var targets = document.querySelectorAll('.your .row-horn');
    toArray(targets).forEach(
        function(hornTarget) {
            hornTarget.classList.toggle('highlighted-target');
        }
    );

}

function clickOnHornTarget(event) {
    ws.send(JSON.stringify({ command: "HORN_TARGET_CLICKED", target: event.currentTarget.id }));
    // playCardSmoothly(hornCard, hornLocation);
    // toggleHighlightHornTargets();
}

function toggleHighlightDecoyTargets () {
    // https://developer.mozilla.org/ru/docs/Web/API/NodeList
    var targets = document.querySelectorAll('.your .card:not([hero])');
    toArray(targets).forEach(
        function(hornTarget) {
            hornTarget.classList.toggle('highlighted-target');
        }
    );

}
function switchCards(obj1, obj2) {
    obj1.classList.toggle('hidden');
    obj2.classList.toggle('hidden');

    setTimeout(
        function() {
            var temp = document.createElement("div");
            obj1.parentNode.insertBefore(temp, obj1);

            // move obj1 to right before obj2
            obj2.parentNode.insertBefore(obj1, obj2);

            // move obj2 to right before where obj1 used to be
            temp.parentNode.insertBefore(obj2, temp);

            // remove temporary marker node
            temp.parentNode.removeChild(temp);
            var m = obj1.style.marginLeft;
            obj1.style.marginLeft = obj2.style.marginLeft;
            obj2.style.marginLeft = m;
            setTimeout(
                function() {
                    obj1.classList.toggle('hidden');
                    obj2.classList.toggle('hidden');
                }, 200);
        }, 400);
}

function clickOnCard(event) {
    var card = event.currentTarget;
    lastClickedCard = card;
    ws.send(JSON.stringify({ command: "CARD_CLICKED", target: card.id }));
}

function clickOnCard_OLD(event) {
    // var card = event.currentTarget;
    // TODO: remove after useful code is reused
    // what to highlight or where to play
    // in the manner of 'highlight: id1, id2, id3'

    // https://css-tricks.com/almanac/selectors/a/attribute/
}
function clearWeatherEffects() {
    var weather = document.getElementById("weather-target");
    var currentWeatherEffects = weather.children;
    /* flushElement(weather); */

    toArray(currentWeatherEffects).forEach(
        function(card) { discardCardSmoothly(card, 1000); });
    clearDescription();
}
function discardCardSmoothly(card, timeout) {
    timeout = timeout || 400;
    card.classList.toggle('hidden');
    setTimeout(function(){
        card.parentNode.removeChild(card);
    }, timeout);
}
function playCardSmoothly(card, field) {
    card.classList.toggle('hidden');
    setTimeout(
        function() {
            field.appendChild(card);
            card.style.marginLeft = "0px";
            compactCards(field);
            compactHand();
            setTimeout(
                function() {
                    card.classList.toggle('hidden');
                    if (card.hasAttribute('hero')) {
                        setTimeout(
                            function() {
                                card.classList.toggle('hero');
                            }, 1000);
                    }
                }, 200);
        }, 400);
}
function dealCardSmoothly(card) {
    card.classList.toggle('hidden');
    var hand = document.getElementById("hand");
    setTimeout(
        function() {
            hand.appendChild(card);
            compactHand();
            setTimeout(
                function() {
                    card.classList.toggle('hidden');
                }, 200);
        }, 400);
}

function clearDescription() {
    var bc = document.getElementById('big-card');
    bc.style.backgroundImage = '';
    var d = document.getElementById('description-text');
    d.value = '';

}
function addCardsFaces() {
    var cards = document.querySelectorAll('.card');
    console.log(cards);
    toArray(cards).forEach(function(card){
        if (card.hasAttribute('deck')) {
            addFace(card);
        }
    });
}
function addFace(card) {
    var urlStr = 'url("' + 'resources/' +
        getDeckPartOfFaceUrl(card) + getCardPartOfFaceUrl(card)+ '")';
    card.style.backgroundImage = urlStr;
}
function getDeckPartOfFaceUrl(card) {
    switch (card.getAttribute('deck')) {
        case 'NEUTRAL':
            return 'neutral-deck/';
        case 'NORTHERN':
            return 'northern-deck/';
        default : return '';
    }
}
function getCardPartOfFaceUrl(card) {
    return card.getAttribute('face') + '.png';
}
function askForCards() {
    ws.send(JSON.stringify({command: "DEAL_CARDS"}));
}
function connect() {
    ws = new WebSocket("ws://localhost:8080/json");
    ws.onmessage = onMessage;
}
var lastClickedCard;
function onMessage(msg) {
    var response = JSON.parse(msg.data);
    switch (response.command) {
        case "DEAL_CARDS":
            response.target.forEach(function(card) {
                var cardElement = convertToElement(card);
                addToHand(cardElement);
            });
            compactHand();
            break;
        case "DEAL_CARDS_SMOOTHLY":
            response.target.forEach(function(card) {
                var cardElement = convertToElement(card);
                dealCardSmoothly(cardElement);
            });
            compactHand();
            break;
        case "HIGHLIGHT_ROW":
            console.log(response);
            break;
        case "TOGGLE_HIGHLIGHT_CARDS":
            console.log(response);
            response.target.forEach(function(id){
                document.getElementById(id).classList.toggle('highlighted-target');
            });
            break;
        case "TOGGLE_HIGHLIGHT_HORN_TARGETS":
            console.log(response);
            toggleHighlightHornTargets();
            break;
        case "PLAY_HORN":
            console.log(response);
            var hornTarget = document.getElementById(response.target);
            playCardSmoothly(lastClickedCard, hornTarget);
            break;
        case "PLAY_WEATHER":
            console.log(response);
            var weatherTarget = document.getElementById("weather-target");
            playCardSmoothly(lastClickedCard, weatherTarget);
            break;
        case "SWITCH_CARDS":
            console.log(response);
            var fromCard = document.getElementById(response.target.from);
            var toCard = document.getElementById(response.target.to);
            switchCards(fromCard, toCard);
            break;
        case "UPDATE_ROW_SCORE":
            console.log(response);
            var rows = response.target;
            for (var row in rows) {
                if(rows.hasOwnProperty(row)) {
                    // console.log(row + " has score " + rows[row]);
                    updateRowScore(row, rows[row]);
                }
            }
            break;
        case "PLAY_CARD":
            console.log(response);
            var where = document.querySelector("#" + response.target + ">.played-row");
            if (where && lastClickedCard) {
                playCardSmoothly(lastClickedCard, where);
            }
            break;
        case "PLAY_BONDED_CARD_NEAR":
            console.log(response);
            var bondedCard = document.getElementById(response.target);
            bondedCard.parentNode.insertBefore(lastClickedCard, bondedCard);
            break;
        case "EVENT_IGNORED":
            console.log(response);
            break;
    }
}
function addToHand(card) {
    var hand = document.getElementById("hand");
    hand.appendChild(card);
}
function compactHand() {
    compactCards(document.getElementById('hand'));
}
function convertToElement(card) {
    var div = document.createElement("div");
    div.setAttribute("id", card.id);
    div.setAttribute("row", card.type);
    div.setAttribute("description", card.description);
    div.setAttribute("deck", card.deck);
    div.setAttribute("face", card.face);

    if (card.hero) {
        div.setAttribute("hero", "true");
    }
    if (card.healer) {
        div.setAttribute("healer", "true");
    }
    div.classList.add("card");
    //TODO: allow switching between singleclick and doubleclick from UI
    div.addEventListener("dblclick",
        function (event) {
            return false;
        });
    div.addEventListener("click", clickOnCard);
    addFace(div);
    return div;
}
function compactCards(container) {
    var cards = container.children;
    var count = cards.length;
    if (count < 3) {
        return;
    }
    var cardWidth = cards[0].offsetWidth;
    var containerWidth = container.clientWidth;

    var marginDelta = (containerWidth - 63 - cardWidth * count) / count;
    if (marginDelta > 1) {
        return;
    }

    console.log(marginDelta);
    for (var i = 1; i < cards.length; i++) {
        cards[i].style.marginLeft = marginDelta + "px";
    }
}
function updateRowScore(rowId, score) {
    document.querySelector("#" + rowId + " .row-score").textContent = score;
}
function onresize() {
    compactHand();
    // TODO: compact rows as well
}


function playNear(nearCard) {

}