package com.example.demad.inventoryapp1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import static com.example.demad.inventoryapp1.data.BookContract.BookEntry.*;

/**
 * Allows user to create a new book or edit an existing one.
 */
public class EditorBookActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Identifier for the book data loader
     */
    private static final int EXISTING_BOOK_LOADER = 0;
    /**
     * Content URI for the existing book (null if it's a new book)
     */
    private Uri currentEditorBookUri;
    /**
     * Edit field to enter the book title
     */
    private EditText bookTitleEditText;
    /**
     * EditText field to enter the book price
     */
    private EditText priceEditText;
    /**
     * EditText field to enter the book quantity
     */
    private EditText quantityEditText;
    /**
     * EditText field to enter the book supply name
     */
    private EditText supplyNameEditText;
    /**
     * EditText field to enter the book supply phone
     */
    private EditText supplyPhoneEditText;
    /**
     * Boolean flag that keeps track of whether the book has been edited (true) or not (false)
     */
    private boolean bookHasChanged = false;
    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the bookHasChanged boolean to true.
     */
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            bookHasChanged = true;
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        /*
        Examine the intent that was used to launch this activity,
        in order to figure out if we're creating a new book or editing an existing one.
        */
        currentEditorBookUri = getIntent().getData();
        /*
        If the intent DOES NOT contain a book content URI, then we know that we are
        creating a new book.
        */
        if (currentEditorBookUri == null) {
            // This is a new book, so change the app bar to say "Add a Book"
            setTitle("Add a Book");
        } else {
            setTitle("Edit Book");
            // Initialize a loader to read the book data from the database
            // and display the current values in the editor
            getSupportLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }
        // Find all relevant views that we will need to read user input from
        bookTitleEditText = findViewById(R.id.title_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        supplyNameEditText = findViewById(R.id.supply_name_edit_text);
        supplyPhoneEditText = findViewById(R.id.supply_phone_edit_text);
        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        bookTitleEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);
        quantityEditText.setOnTouchListener(touchListener);
        supplyNameEditText.setOnTouchListener(touchListener);
        supplyPhoneEditText.setOnTouchListener(touchListener);
    }

    /**
     * Get user input from editor and save new book into database.
     */
    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String bookTitleString = bookTitleEditText.getText().toString().trim();
        String bookPriceString = priceEditText.getText().toString().trim();
        String bookQuantityString = quantityEditText.getText().toString().trim();
        String bookSupplyNameString = supplyNameEditText.getText().toString().trim();
        String bookSupplyPhoneString = supplyPhoneEditText.getText().toString().trim();
        // Check if this is supposed to be a new book
        // and check if all the fields in the editor are blank
        if (currentEditorBookUri == null) {
            if (TextUtils.isEmpty(bookTitleString)) {
                Toast.makeText(this, "Book title required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookPriceString)) {
                Toast.makeText(this, "Book price required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookQuantityString)) {
                Toast.makeText(this, "Book quantity required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookSupplyNameString)) {
                Toast.makeText(this, "Supply name required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookSupplyPhoneString)) {
                Toast.makeText(this, "Supply phone required!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Create a ContentValues object where column names are the keys,
            // and book attributes from the editor are the values.
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_BOOK_TITLE, bookTitleString);
            contentValues.put(COLUMN_BOOK_PRICE, bookPriceString);
            contentValues.put(COLUMN_BOOK_QUANTITY, bookQuantityString);
            contentValues.put(COLUMN_BOOK_SUPPLY_NAME, bookSupplyNameString);
            contentValues.put(COLUMN_BOOK_SUPPLY_PHONE, bookSupplyPhoneString);
            Uri newUri = getContentResolver().insert(CONTENT_URI, contentValues);
            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Book saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            if (TextUtils.isEmpty(bookTitleString)) {
                Toast.makeText(this, "Book title required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookPriceString)) {
                Toast.makeText(this, "Book price required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookQuantityString)) {
                Toast.makeText(this, "Book quantity required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookSupplyNameString)) {
                Toast.makeText(this, "Supply name required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookSupplyPhoneString)) {
                Toast.makeText(this, "Supply phone required!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Create a ContentValues object where column names are the keys,
            // and book attributes from the editor are the values.
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_BOOK_TITLE, bookTitleString);
            contentValues.put(COLUMN_BOOK_PRICE, bookPriceString);
            contentValues.put(COLUMN_BOOK_QUANTITY, bookQuantityString);
            contentValues.put(COLUMN_BOOK_SUPPLY_NAME, bookSupplyNameString);
            contentValues.put(COLUMN_BOOK_SUPPLY_PHONE, bookSupplyPhoneString);
            int rowsAffected = getContentResolver().update(currentEditorBookUri, contentValues, null, null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Error with updating book", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "Book updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /*
     * This adds menu items to the app bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/editor_menu.xml file.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //Save Book to the database
                saveBook();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the book hasn't changed, continue with navigating up to parent activity
                // which is the {@link MainActivity}.
                if (!bookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorBookActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorBookActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    public void onBackPressed() {
        // If the book hasn't changed, continue with handling back button press
        if (!bookHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Since the editor shows all book attributes, define a projection that contains
        // all columns from the book table
        String[] projection = {
                _ID,
                COLUMN_BOOK_TITLE,
                COLUMN_BOOK_PRICE,
                COLUMN_BOOK_QUANTITY,
                COLUMN_BOOK_SUPPLY_NAME,
                COLUMN_BOOK_SUPPLY_PHONE};
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentEditorBookUri,          // Query the content URI for the current book
                projection,                    // Columns to include in the resulting Cursor
                null,                  // No selection clause
                null,               // No selection arguments
                null);                 // Default sort order
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // Release early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading cursor from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of book attributes that we're interested in
            int titleColumnIndex = cursor.getColumnIndex(COLUMN_BOOK_TITLE);
            int priceColumnIndex = cursor.getColumnIndex(COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(COLUMN_BOOK_QUANTITY);
            int supplyNameColumnIndex = cursor.getColumnIndex(COLUMN_BOOK_SUPPLY_NAME);
            int supplyPhoneColumnIndex = cursor.getColumnIndex(COLUMN_BOOK_SUPPLY_PHONE);
            // Extract out the value from the Cursor for the given column index
            String bookTitle = cursor.getString(titleColumnIndex);
            int bookPrice = cursor.getInt(priceColumnIndex);
            int bookQuantity = cursor.getInt(quantityColumnIndex);
            String supplyName = cursor.getString(supplyNameColumnIndex);
            String supplyPhone = cursor.getString(supplyPhoneColumnIndex);
            // Update the views on the screen with the values from the database
            bookTitleEditText.setText(bookTitle);
            priceEditText.setText(Integer.toString(bookPrice));
            quantityEditText.setText(Integer.toString(bookQuantity));
            supplyNameEditText.setText(supplyName);
            supplyPhoneEditText.setText(supplyPhone);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        bookTitleEditText.setText("");
        priceEditText.setText("");
        quantityEditText.setText("");
        supplyNameEditText.setText("");
        supplyPhoneEditText.setText("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("Discard", discardButtonListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the book.
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
