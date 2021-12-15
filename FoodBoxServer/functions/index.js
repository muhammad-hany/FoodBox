
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();



exports.sendRestarantMessage = functions.https.onRequest((req, res) => {
    const orderId = String(req.query.orderId);

    admin.firestore().doc(`orders/${orderId}`).get()
        .then(snapshot => {

            const restaurantId = String(snapshot.data().restaurantId);
            admin.firestore().doc(`restaurants/${restaurantId}`).get()
                .then(restaurantSnapshot => {
                    const token = restaurantSnapshot.data().token;
                    console.log(`token is ${token}`)
                    const payload = {
                        notification: {
                            title: 'You have a new Order! from sendMessage !',
                            body: `order number is ${orderId}`,
                        }
                    };

                    return admin.messaging().sendToDevice(token, payload)
                }).catch(error => {
                    console.log(error);
                    res.status(500).send(error);
                })


        }).catch(error => {
            console.log(error);
            res.status(500).send(error);
        })

});

exports.OnNewOrder = functions.firestore.document('orders/{orderId}').onCreate((snap, context) => {
    const order = snap.data();
    const restaurantId = String(order.restaurantId);
    console.log(`restaurantId is ${restaurantId}`)
    admin.firestore().doc(`restaurants/${restaurantId}`).get()
        .then(snapshot => {
            const token = String(snapshot.data().token);
            console.log(`token is ${token}`)
            const payload = {
                notification: {
                    title: 'You have a new Order!',
                    body: `order number is ${order.orderId}`,
                }
            };

            return admin.messaging().sendToDevice(token, payload)
                .catch(error => {
                    console.error('FCM failed', error);
                })
        }).catch(error => {
            console.error('firestore error', error);
        })


});







