const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp(functions.config().firebase);


// eslint-disable-next-line max-len
exports.sendAdminNotification = functions.database.ref("announcements/{pushId}").onCreate((event) => {
  const announcement = event.val();

  const payload = {notification: {
    title: "New Announcement!",
    body: `${announcement.publishment}`,
  },
  };

  // eslint-disable-next-line max-len
  const users = ["Gu1bmemcrChSRVIkB790Eit2Xh82", "ZL7uGXpwxISkovwLYhVYRTL20412", "tBuR5LlwoWaEav7GdGSmx91tNq73"];

  for (let i=0; i<users.length; i++) {
    const ref2 = admin.database().ref(`Users/${users[i]}`);
    if (ref2 != null) {
      ref2.once("value", function(snapshot) {
        if (snapshot.val() != null) {
          admin.messaging().sendToDevice(snapshot.val(), payload).then((ev) => {
            console.log(ev);
          });
        }
      });
    }
  }
});


// eslint-disable-next-line max-len
exports.sendAdminNotification2 = functions.database.ref("assignments/{pushId}").onCreate((event) => {
  const announcement = event.val();

  const payload = {
    notification: {
      title: "New Assignment!",
      body: `${announcement.publishment}`,
    },
  };

  // eslint-disable-next-line max-len
  const users = ["Gu1bmemcrChSRVIkB790Eit2Xh82", "ZL7uGXpwxISkovwLYhVYRTL20412", "tBuR5LlwoWaEav7GdGSmx91tNq73"];

  for (let i=0; i<users.length; i++) {
    const ref2 = admin.database().ref(`Users/${users[i]}`);
    if (ref2 != null) {
      ref2.once("value", function(snapshot) {
        if (snapshot.val() != null) {
          admin.messaging().sendToDevice(snapshot.val(), payload).then((ev) => {
            console.log(ev);
          });
        }
      });
    }
  }
});


