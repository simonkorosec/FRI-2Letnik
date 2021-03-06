import QtQuick 2.0
import QtMultimedia 5.8

Item {
    id: root
    property string colorCorrect
    property string colorWrong
    property string colorKey
    property string colorText
    property string crka: model.crka
    property bool isCorectPos: true


    width: 75; height: 75

    MouseArea {
        id: mouseArea

        width: parent.width; height: parent.height
        anchors.centerIn: parent

        drag.target: tile

        //        onReleased: parent = tile.Drag.target !== null ? tile.Drag.target : root

        onReleased: drop()

        Rectangle {
            id: tile

            width: parent.width; height: parent.height
            anchors.verticalCenter: parent.verticalCenter
            anchors.horizontalCenter: parent.horizontalCenter

            color: colorKey

            Drag.keys: [ colorKey ]
            Drag.active: mouseArea.drag.active
            Drag.hotSpot.x: 32
            Drag.hotSpot.y: 32

            Text {
                anchors.fill: parent
                color: colorText
                font.pixelSize: 48
                text: model.crka
                horizontalAlignment:Text.AlignHCenter
                verticalAlignment: Text.AlignVCenter
                font.capitalization: model.capitalization
            }

            states: [
                State {
                    when: mouseArea.drag.active
                    ParentChange { target: tile; parent: root }
                    AnchorChanges { target: tile; anchors.verticalCenter: undefined; anchors.horizontalCenter: undefined }
                },
                State {
                    name: "wrongPos"
                    when: isCorectPos === false
                    PropertyChanges {
                        target: tile
                        width: root.width + 10
                        height: root.height + 10
                    }
                }
            ]
        }

        function drop(){
            var prevPar = parent;
            parent = tile.Drag.target !== null ? tile.Drag.target : root;

            if (parent !== root && parent.crka === root.crka) {
                //console.log("uspeh");
                root.isCorectPos = true;
                parent.isCorectPos = true;

                tile.color = colorCorrect;

                //console.log(parent.parent.stPravilnih);
                lvl.preveriPravilnost();

                correctMove.play();
            } else if (parent !== root && parent.crka !== root.crka){
                //console.log("ne");
                prevPar.isCorectPos = false;
                root.isCorectPos = false;

                tile.color = colorWrong;
                wrongMove.play();
            } else if (parent === root) {
                prevPar.isCorectPos = false;
                root.isCorectPos = true;

                tile.color = colorKey;
            }
        }
    }

    SoundEffect {
        id: correctMove
        source: "sounds/correct-move.wav"
    }

    SoundEffect {
        id: wrongMove
        source: "sounds/wrong-move.wav"
    }

}
