import {checkLoginStatus, storeOrderViewId} from "./common.js";

// checkLoginStatus();


const token = localStorage.getItem("token");

async function getOrders() {
    const ordersList = document.getElementById("orders-list");

    await fetch("http://localhost:8080/business/orders", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        }
    })
        .then(res => res.json()).then(orders => {
            orders.forEach(order => {



                let orderDiv = document.createElement("div");
                "m-4 container border border-2 rounded p-2".split(" ").forEach(styleClass => {
                    orderDiv.classList.add(styleClass)
                });

                orderDiv.innerHTML = `
                    <a style="text-decoration: none; color:black" href="order-view.html" id="order-view-${order.id}">
                        <h3>Order number: ${order.id}</h3>
                        <p>Ordered on: ${order.orderedOn_date}</p>
                        <p>Delivered to: ${order.address}</p>
                        <p class="fw-bolder">Total: ${order.orderPrice}</p>
                    </a>
                `;



                ordersList.appendChild(orderDiv);

                const orderDivLink = document.getElementById(`order-view-${order.id}`);
                orderDivLink.addEventListener("click", () => {
                    storeOrderViewId(order.id);
                })



            })
        })

}

getOrders();
