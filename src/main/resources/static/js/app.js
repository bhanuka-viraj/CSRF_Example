// Initialize when document is ready
$(document).ready(function() {
    // Check authentication status on page load
    checkAuthStatus();

    // Handle login form submission
    $('#login-form').submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: '/login',
            type: 'POST',
            data: $(this).serialize(),
            success: function() {
                // Update UI and refresh CSRF token on successful login
                updateUI(true);
                checkAuthStatus(); // Re-check status to ensure consistency
            },
            error: function(xhr) {
                alert('Login failed: ' + xhr.responseText);
            }
        });
    });

    // Handle logout
    $('#logout-btn').click(function() {
        $.ajax({
            url: '/logout',
            type: 'POST',
            headers: {
                'X-XSRF-TOKEN': getCsrfToken()
            },
            success: function() {
                // Update UI to unauthenticated state
                updateUI(false);
                // Clear any residual results
                $('#protected-result').text('');
                $('#public-result').text('');
            },
            error: function(xhr) {
                alert('Logout failed: ' + xhr.responseText);
            }
        });
    });
});

// Check authentication status by attempting to access a protected endpoint
function checkAuthStatus() {
    $.get('/api/user/profile')
        .done(function() {
            updateUI(true);
        })
        .fail(function() {
            updateUI(false);
        });
}

// Update UI based on authentication status
function updateUI(isAuthenticated) {
    if (isAuthenticated) {
        $('#auth-title').text('Welcome');
        $('#login-form').hide();
        $('#logout-btn').show();
        $('#protected-section').show();
    } else {
        $('#auth-title').text('Login');
        $('#login-form').show();
        $('#logout-btn').hide();
        $('#protected-section').hide();
        $('#protected-result').text('');
    }
}

// Get CSRF token from cookie
function getCsrfToken() {
    const name = 'XSRF-TOKEN=';
    const decodedCookie = decodeURIComponent(document.cookie);
    const ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return '';
}

// Fetch user profile (protected GET request)
function fetchProfile() {
    $.ajax({
        url: '/api/user/profile',
        type: 'GET',
        success: function(response) {
            $('#protected-result').text(response);
        },
        error: function(xhr) {
            $('#protected-result').text('Error: ' + xhr.responseText);
        }
    });
}

// Update user profile (protected POST request with CSRF token)
function updateProfile() {
    const token = getCsrfToken();
    $.ajax({
        url: '/api/user/profile',
        type: 'POST',
        headers: {
            'X-XSRF-TOKEN': token
        },
        contentType: 'application/json',
        data: JSON.stringify({ data: 'Updated profile data' }),
        success: function(response) {
            $('#protected-result').text(response);
        },
        error: function(xhr) {
            $('#protected-result').text('Error: ' + xhr.responseText);
        }
    });
}

// Fetch public information (no CSRF required)
function fetchPublicInfo() {
    $.ajax({
        url: '/api/public/info',
        type: 'GET',
        success: function(response) {
            $('#public-result').text(response);
        },
        error: function(xhr) {
            $('#public-result').text('Error: ' + xhr.responseText);
        }
    });
}