const accessToken = localStorage.getItem('accessToken');

document.getElementById('create-item-form').addEventListener('submit', async function (e) {
    e.preventDefault();

    const item = {
        name: document.getElementById('name').value,
        description: document.getElementById('description').value,
        price: parseInt(document.getElementById('price').value),
        type: document.getElementById('type').value,
        isStackable: document.getElementById('isStackable').checked
    };

    const response = await fetch('/admin/item', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + accessToken
        },
        body: JSON.stringify(item)
    });

    if (response.ok) {
        alert('Item created!');
        window.location.href = '/admin/index.html';
    } else {
        alert('Failed to create item');
    }
});
