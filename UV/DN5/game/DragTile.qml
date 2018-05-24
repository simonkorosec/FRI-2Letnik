import QtQuick 2.0

Item {
    id: root
    property string colorKey
    property string crka: modelData
    property bool isCorectPos: true

    width: 64; height: 64

    MouseArea {
        id: mouseArea

        width: 64; height: 64
        anchors.centerIn: parent

        drag.target: tile

        //        onReleased: parent = tile.Drag.target !== null ? tile.Drag.target : root

        onReleased: drop()

        Rectangle {
            id: tile

            width: 64; height: 64
            anchors.verticalCenter: parent.verticalCenter
            anchors.horizontalCenter: parent.horizontalCenter

            color: colorKey

            Drag.keys: [ colorKey ]
            Drag.active: mouseArea.drag.active
            Drag.hotSpot.x: 32
            Drag.hotSpot.y: 32

            Text {
                anchors.fill: parent
                color: "white"
                font.pixelSize: 48
                text: modelData
                horizontalAlignment:Text.AlignHCenter
                verticalAlignment: Text.AlignVCenter
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
                        width: 74
                        height: 74
                    }
                }
            ]
        }

        function drop(){
            parent = tile.Drag.target !== null ? tile.Drag.target : root;

            if (parent !== root && parent.crka === root.crka) {
                console.log("uspeh");
                root.isCorectPos = true;
            } else if (parent !== root && parent.crka !== root.crka){
                console.log("ne");
                root.isCorectPos = false;
            } else if (parent === root) {
                root.isCorectPos = true;
            }
        }
    }
}
