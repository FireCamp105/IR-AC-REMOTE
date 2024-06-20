(function() {
  const admin = require("firebase-admin");
  const serviceAccount = require("./serviceAccountKey.json");



  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "Use Yours",
    apiKey: "Use Yours",
    messagingSenderId: "Use Yours"
  });

  console.log("Script started!");

  console.log("Firebase Admin SDK initialized");

  const db = admin.database();

  const ref2 = db.ref("temp");
  console.log("Listening for RTDB temp value changes...");
  ref2.on("value", function(snapshot) {
    var temp = snapshot.val();
    console.log("The temp is now:", temp);
  });


  const ref = db.ref("alarm");

  console.log("Listening for RTDB alarm value changes...");

  ref.on("value", function(snapshot) {
    var alarm = snapshot.val();
    if (alarm) {
      var title = "Alert!";
      var body = "detected person";
      console.log("New notification detected, sending FCM message...");

      admin.messaging().send({
        notification: {
          title: title,
          body: body
        },
        topic: "news"
      });
    }
  });
})();