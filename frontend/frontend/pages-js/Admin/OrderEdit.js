export default async function orderEdit(orderId) {

    const response = await fetch(`http://localhost:8080/business/orders/${orderId}`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`
        }
    });

    if (!response.ok) {
        console.error("Failed to fetch order:", response.status);
        return null;
    }

    const order = await response.json();

    const orderStatuses = [
        "PENDING",
        "PROCESSING",
        "SHIPPED",
        "DELIVERED",
        "CANCELLED"
    ];

    // Change these options to match your PaymentStatus enum.
    const paymentStatuses = [
        "UNPAID",
        "PAID"
    ];

    return `
        <section class="orders">

            <h2>Edit Order #${String(order.id).padStart(5, "0")}</h2>

            <form id="edit-order-form">

                <input type="hidden" id="order-id" value="${order.id}">

                <h3>Customer Information</h3>

                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email"
                           value="${order.email ?? ""}" required>
                </div>

                <div class="form-group">
                    <label for="firstName">First name</label>
                    <input type="text" id="firstName" name="firstName"
                           value="${order.firstName ?? ""}" required>
                </div>

                <div class="form-group">
                    <label for="lastName">Last name</label>
                    <input type="text" id="lastName" name="lastName"
                           value="${order.lastName ?? ""}" required>
                </div>

                <div class="form-group">
                    <label for="company">Company</label>
                    <input type="text" id="company" name="company"
                           value="${order.company ?? ""}">
                </div>

                <div class="form-group">
                    <label for="phoneNumber">Phone number</label>
                    <input type="text" id="phoneNumber" name="phoneNumber"
                           value="${order.phoneNumber ?? ""}" required>
                </div>

                <h3>Delivery Information</h3>

                <div class="form-group">
                    <label for="address">Address</label>
                    <input type="text" id="address" name="address"
                           value="${order.address ?? ""}" required>
                </div>

                <div class="form-group">
                    <label for="city">City</label>
                    <input type="text" id="city" name="city"
                           value="${order.city ?? ""}" required>
                </div>

                <div class="form-group">
                    <label for="country">Country</label>
                    <input type="text" id="country" name="country"
                           value="${order.country ?? ""}" required>
                </div>

                <div class="form-group">
                    <label for="postalCode">Postal code</label>
                    <input type="text" id="postalCode" name="postalCode"
                           value="${order.postalCode ?? ""}" required>
                </div>

                <div class="form-group">
                    <label for="expectedDelivery">Expected delivery</label>
                    <input type="date" id="expectedDelivery"
                           name="expectedDelivery"
                           value="${order.expectedDelivery ?? ""}">
                </div>

                <h3>Order Information</h3>

                <div class="form-group">
                    <label for="orderStatus">Order status</label>
                    <select id="orderStatus" name="orderStatus">
                        ${
        orderStatuses.map(status => `
                                <option value="${status}"
                                    ${order.orderStatus === status ? "selected" : ""}>
                                    ${status}
                                </option>
                            `).join("")
    }
                    </select>
                </div>

                <div class="form-group">
                    <label for="paymentStatus">Payment status</label>
                    <select id="paymentStatus" name="paymentStatus">
                        ${
        paymentStatuses.map(status => `
                                <option value="${status}"
                                    ${order.paymentStatus === status ? "selected" : ""}>
                                    ${status}
                                </option>
                            `).join("")
    }
                    </select>
                </div>

                <div class="form-group">
                    <label for="orderPrice">Order price (€)</label>
                    <input type="number" id="orderPrice" name="orderPrice"
                           value="${order.orderPrice ?? 0}"
                           step="0.01" readonly>
                </div>

                <div class="form-group">
                    <label for="orderedOnDate">Ordered on</label>
                    <input type="text" id="orderedOnDate"
                           value="${order.orderedOn_date?.replace("T", " ").slice(0, 16) ?? ""}"
                           readonly>
                </div>

                <div class="form-actions">

                    <button type="submit" class="save-btn">
                        Save Changes
                    </button>

                    <button type="button" id="order-edit-cancel-btn"
                            class="cancel-btn">
                        Cancel
                    </button>

                </div>

            </form>

        </section>
    `;
}