export default async function ordersAdmin(){

    const response = await fetch("http://localhost:8080/business/orders/all", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
        }
    });

    if(!response.ok){
        throw new Error(`Request failed: ${response.status}`);
    }

    const orders = await response.json();
    orders.sort((a, b) =>
        new Date(b.orderedOn_date) - new Date(a.orderedOn_date)
    );
    return `
    <section class="orders" id="orders">
        <h2>Orders</h2>

        <table>
            <thead>
            <tr class="table-columns">
                <th>Order ID</th>
                <th>Customer</th>
                <th>Order price</th>
                <th>Ordered on</th>
                <th>Payment status</th>
                <th>Order status</th>
            </tr>
            </thead>

            <tbody>
                ${orders.map(order =>{
                    let iso = order.orderedOn_date;
                    const [year, month, day] = iso.slice(0, 10).split("-");
                    let formattedOrderedOn = `${day}.${month}.${year}`;
            
                    let orderStatus = document.createElement("p");
                    orderStatus.innerText = `${order.orderStatus}`;
            
                    orderStatus.classList.add("table-colours");
            
                    switch (order.orderStatus) {
                        case "PENDING":
                            orderStatus.classList.add("yellow");
                            break;
            
                        case "PROCESSING":
                            orderStatus.classList.add("light-blue");
                            break;
            
                        case "SHIPPED":
                            orderStatus.classList.add("blue");
                            break;
            
                        case "DELIVERED":
                            orderStatus.classList.add("green");
                            break;
            
                        case "CANCELLED":
                            orderStatus.classList.add("red");
                            break;
            
                        default:
                            orderStatus.classList.add("grey");
            
                    }
            
            
                    let paymentStatus = document.createElement("p");
                    paymentStatus.innerText = `${order.paymentStatus}`;
            
                    paymentStatus.classList.add("table-colours");
            
                    switch (order.paymentStatus) {
                        case "UNPAID":
                            paymentStatus.classList.add("light-blue");
                            break;
            
                        case "PAID":
                            paymentStatus.classList.add("green");
                            break;
            
                        case "REFUNDED":
                            paymentStatus.classList.add("purple");
                            break;
            
                        case "FAILED":
                            paymentStatus.classList.add("red");
                            break;
            
                        case "CANCELLED":
                            paymentStatus.classList.add("grey");
                            break;
            
                        default:
                            paymentStatus.classList.add("grey");
            
                    }
                    return `
                        <tr>
                            <td># ${order.id}</td>
                            <td>${order.firstName} ${order.lastName}</td>
                            <td>€ ${order.orderPrice.toFixed(2)}</td>
                            <td>${formattedOrderedOn}</td>
                            <td>${paymentStatus.outerHTML}</td>
                            <td>${orderStatus.outerHTML}</td>
                            <td>
                            <button class="util-btn edit-btn" id="order-edit-btn-${order.id}">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-fill" viewBox="0 0 16 16">
                                  <path d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.5.5 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11z"/>
                                </svg>
                            </button>
                            <button class="util-btn trash-btn">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash3-fill" viewBox="0 0 16 16">
                                  <path d="M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5m-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5M4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06m6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528M8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5"/>
                                </svg>
                            </button>
                            
                            </td>
                            
                        </tr>
                `}).join("")}
            </tbody>
        </table>
    </section>
`;}