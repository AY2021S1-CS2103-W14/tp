package seedu.clinic.model;

import static java.util.Objects.requireNonNull;
import static seedu.clinic.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.clinic.commons.core.GuiSettings;
import seedu.clinic.commons.core.LogsCenter;
import seedu.clinic.model.supplier.Supplier;

/**
 * Represents the in-memory model of the clinic data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Clinic clinic;
    private final UserPrefs userPrefs;
    private final FilteredList<Supplier> filteredSuppliers;

    /**
     * Initializes a ModelManager with the given clinic and userPrefs.
     */
    public ModelManager(ReadOnlyClinic clinic, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(clinic, userPrefs);

        logger.fine("Initializing with clinic: " + clinic + " and user prefs " + userPrefs);

        this.clinic = new Clinic(clinic);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredSuppliers = new FilteredList<>(this.clinic.getSupplierList());
    }

    public ModelManager() {
        this(new Clinic(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getClinicFilePath() {
        return userPrefs.getClinicFilePath();
    }

    @Override
    public void setClinicFilePath(Path ClinicFilePath) {
        requireNonNull(ClinicFilePath);
        userPrefs.setClinicFilePath(ClinicFilePath);
    }

    //=========== Clinic ================================================================================

    @Override
    public void setClinic(ReadOnlyClinic clinic) {
        this.clinic.resetData(clinic);
    }

    @Override
    public ReadOnlyClinic getClinic() {
        return clinic;
    }

    @Override
    public boolean hasSupplier(Supplier supplier) {
        requireNonNull(supplier);
        return clinic.hasSupplier(supplier);
    }

    @Override
    public void deleteSupplier(Supplier target) {
        clinic.removeSupplier(target);
    }

    @Override
    public void addSupplier(Supplier supplier) {
        clinic.addSupplier(supplier);
        updateFilteredSupplierList(PREDICATE_SHOW_ALL_SUPPLIERS);
    }

    @Override
    public void setSupplier(Supplier target, Supplier editedSupplier) {
        requireAllNonNull(target, editedSupplier);

        clinic.setSupplier(target, editedSupplier);
    }

    //=========== Filtered Supplier List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Supplier} backed by the internal list of
     * {@code versionedClinic}
     */
    @Override
    public ObservableList<Supplier> getFilteredSupplierList() {
        return filteredSuppliers;
    }

    @Override
    public void updateFilteredSupplierList(Predicate<Supplier> predicate) {
        requireNonNull(predicate);
        filteredSuppliers.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return clinic.equals(other.clinic)
                && userPrefs.equals(other.userPrefs)
                && filteredSuppliers.equals(other.filteredSuppliers);
    }

}