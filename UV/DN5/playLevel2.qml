import QtQuick 2.0
import QtQuick.Layouts 1.3
import QtQuick.Controls 2.2
import QtMultimedia 5.8

Rectangle {
    id: lvl
    color: "lightgrey"
    width: 1280
    height: 800
    property string colorCorrect
    property string colorWrong
    property string barva: "black"
    property string barvaPloscic: "red"
    property string barvaCrk: "white"
    property string beseda: "avto"

    ColumnLayout {
        anchors.fill: parent

        RowLayout {
            ColumnLayout {
                Rectangle {
                    Button {
                        id: nazajBtn
                        text: "Nazaj"
                        font.capitalization: Font.AllUppercase
                        font.wordSpacing: 1
                        font.letterSpacing: 0
                        padding: 12
                        font.pointSize: 9
                        anchors.top: parent.top
                        anchors.topMargin: 5
                        anchors.left: parent.left
                        anchors.leftMargin: 5

                        onClicked: {
                            //console.log("Odpri choseWord.qml");
                            correctMove.play();
                            lvl.parent.lvlOpened = false;
                            lvl.destroy(1);
                        }

                    }

                    id: pomoc
                    color: barva  //"blue"
                    Layout.preferredHeight: lvl.height/3
                    Layout.fillWidth: true

                    Text {
                        id: pomocTekst
                        anchors.centerIn: parent
                        text: "napaka"
                        font.pointSize: 24
                        font.capitalization: Font.AllUppercase
                        font.letterSpacing: 16
                    }
                }
                Rectangle {
                    id: vnos
                    color: barva //"green"
                    Layout.preferredHeight: lvl.height/3
                    Layout.fillWidth: true

                    Row {
                        id: crkeDestination
                        property string name: "row"
                        property ListModel arr: ListModel {}

                        anchors.verticalCenter: parent.verticalCenter
                        anchors.left: parent.left
                        anchors.leftMargin: 64*2
                        anchors.margins: 5

                        width: 64
                        spacing: 10

                        Repeater {
                            model: vnosModel
                            delegate: DropTile {
                                colorKey: lvl.barvaPloscic
                                colorText: lvl.barvaCrk
                                displayLetter: false
                            }
                        }

                        states: State {
                            when: stPravilnih === 1
                            StateChangeScript {
                                script: preveriPravilnost()
                            }
                        }
                    }

                    ListModel {
                        id: vnosModel
                    }

                }
            }

            Rectangle { // Rectangle -> Item
                id: slika
                Layout.preferredHeight: lvl.height * 2/3
                Layout.preferredWidth: lvl.width / 3
                color: barva //"red"
                Image {
                    id: slikaImg
                    anchors.verticalCenter: parent.verticalCenter
                    //source: "./images/raca.png"
                    width: parent.width
                    fillMode: Image.PreserveAspectFit
                }
            }
        }

        Rectangle {
            id: crke
            Layout.preferredHeight: lvl.height/3
            Layout.fillWidth: true
            color: barva  //"yellow"
            anchors.left: parent.left
            anchors.right: parent.right


            Row {
                id: crkeSource

                //anchors.left: parent.left
                anchors.verticalCenter: parent.verticalCenter
                anchors.left: parent.left
                anchors.leftMargin: 64*2

//                anchors.margins: 5
                width: 64
                spacing: 10

                Repeater {
                    model: crkeModel
                    delegate: DragTile { colorKey: lvl.barvaPloscic
                        colorText: lvl.barvaCrk
                        colorCorrect: lvl.colorCorrect
                        colorWrong: lvl.colorWrong
                    }
                }
            }

            ListModel {
                id: crkeModel
            }

        }
    }


    Component.onCompleted: {
        readSettings();
    }

    function readSettings(){
        var settings = new XMLHttpRequest();
        settings.open('GET', './json/settings.json');
        settings.onreadystatechange = function() {
            if (settings.readyState === XMLHttpRequest.DONE) {
                if (settings.status && settings.status === 200) {
                    //console.log("response", settings.responseText)

                    var result = JSON.parse(settings.responseText);
                    lvl.barva = result["bgColor"];
                    var cap = result["fontCapitalization"];
                    lvl.barvaPloscic = result["barvaPloscic"];
                    lvl.barvaCrk = result["barvaCrk"];
                    lvl.colorCorrect = result["colorCorrect"];
                    lvl.colorWrong = result["colorWrong"];

                    var iskanaBeseda = beseda;
                    readWord(cap, iskanaBeseda);
                } else {
                    console.log("HTTP:", settings.status, settings.statusText);
                }
            }
        }
        settings.send();
    }

    function readWord(capitalization, iskanaBeseda){
        if (capitalization === "Font.AllUppercase"){
            capitalization = Font.AllUppercase;
        } else {
            capitalization = Font.AllLowercase;
        }

        var request = new XMLHttpRequest();
        request.open('GET', './json/words.json');
        request.onreadystatechange = function() {
            if (request.readyState === XMLHttpRequest.DONE) {
                if (request.status && request.status === 200) {
                    //console.log("response", request.responseText)
                    var result = JSON.parse(request.responseText);
                    var list = result["slike"];
                    for (var i in list) {
                        if (list[i].tekst === iskanaBeseda.toLowerCase()){
                            pomocTekst.text = iskanaBeseda;
                            pomocTekst.font.capitalization = capitalization;

                            slikaImg.source = list[i].slika;

                            pomesajCrke(iskanaBeseda);
                            return;
                        }
                    }
                } else {
                    console.log("HTTP:", request.status, request.statusText);
                }
            }
        }
        request.send();
    }

    function pomesajCrke(beseda) {
        var a = beseda.split("");
        var n = a.length;

        for(var c in a){
            vnosModel.append({"crka" : a[c], "indeks": c});
        }

        for(var i = n - 1; i > 0; i--) {
            var j = Math.floor(Math.random() * (i + 1));
            var tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }

        beseda = a.join("");

        for(c in a){
            crkeModel.append({"crka" : a[c]});
        }
    }

    function preveriPravilnost() {
        for(var i = 0; i < crkeDestination.arr.count; i++) {
            if (!crkeDestination.arr.get(i).drop.isCorectPos){
                console.log("nese");
                return;
            }
        }
        console.log("vsi pravilni");
        gameFinished.play();
    }

    SoundEffect {
            id: gameFinished
            source: "sounds/ta-da.wav"
        }

}
