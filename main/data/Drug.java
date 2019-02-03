package main.data;

import java.util.ArrayList;
import java.util.List;

public class Drug extends Data {

    private String name;
    private boolean oral;
    private boolean sublingual;
    private boolean intravenus;
    private boolean intramuscular;
    private boolean subcutaneous;
    private boolean rectal;
    private String action;

    private List<DrugField> brandNames = new ArrayList<>();
    private List<DrugField> classes = new ArrayList<>();
    private List<DrugField> indications = new ArrayList<>();
    private List<DrugField> contraindications = new ArrayList<>();
    private List<DrugField> sideEffects = new ArrayList<>();
    private List<DrugField> considerations = new ArrayList<>();

    public Drug() {
        super(0);
        name = "";
        oral = false;
        sublingual = false;
        intravenus = false;
        intramuscular = false;
        subcutaneous = false;
        rectal = false;
        action = "";
    }

    public Drug(String name, boolean oral, boolean sublingual, boolean intravenus, boolean intramuscular,
                boolean subcutaneous, boolean rectal, String action) {
        super(0);
        this.name = name;
        this.oral = oral;
        this.sublingual = sublingual;
        this.intravenus = intravenus;
        this.intramuscular = intramuscular;
        this.subcutaneous = subcutaneous;
        this.rectal = rectal;
        this.action = action;
    }

    public Drug(int id, String name, boolean oral, boolean sublingual, boolean intravenus, boolean intramuscular,
                boolean subcutaneous, boolean rectal, String action) {
        super(id);
        this.name = name;
        this.oral = oral;
        this.sublingual = sublingual;
        this.intravenus = intravenus;
        this.intramuscular = intramuscular;
        this.subcutaneous = subcutaneous;
        this.rectal = rectal;
        this.action = action;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object getFromMethod(String attributeName) {
        switch (attributeName) {
            case "drugID": return getId();
            case "name": return getName();
            case "oral": return isOral();
            case "sublingual": return isSublingual();
            case "intravenus": return isIntravenus();
            case "intramuscular": return isIntramuscular();
            case "subcutaneous": return isSubcutaneous();
            case "rectal": return isRectal();
            case "action": return getAction();
            default: return null;
        }
    }

    public String getDrugFieldsString(List<DrugField> drugFields) {
        List<String> strings = new ArrayList<>();
        for (DrugField drugField : drugFields) {
            strings.add(drugField.toString());
        }
        return String.join(", ", strings);
    }

    public boolean noRoutes() {
        return !oral && !sublingual && !intravenus && !intramuscular && !rectal && !subcutaneous;
    }

    public String getRoutesString() {
        List<String> routeStrings = new ArrayList<>();
        if (oral)
            routeStrings.add("Oral");
        if (sublingual)
            routeStrings.add("Sublingual");
        if (intravenus)
            routeStrings.add("Intravenous");
        if (intramuscular)
            routeStrings.add("Intramuscular");
        if (rectal)
            routeStrings.add("Rectal");
        if (subcutaneous)
            routeStrings.add("Subcutaneous");
        return String.join(", ", routeStrings);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOral() {
        return oral;
    }

    public void setOral(boolean oral) {
        this.oral = oral;
    }

    public boolean isSublingual() {
        return sublingual;
    }

    public void setSublingual(boolean sublingual) {
        this.sublingual = sublingual;
    }

    public boolean isIntravenus() {
        return intravenus;
    }

    public void setIntravenus(boolean intravenus) {
        this.intravenus = intravenus;
    }

    public boolean isIntramuscular() {
        return intramuscular;
    }

    public void setIntramuscular(boolean intramuscular) {
        this.intramuscular = intramuscular;
    }

    public boolean isSubcutaneous() {
        return subcutaneous;
    }

    public void setSubcutaneous(boolean subcutaneous) {
        this.subcutaneous = subcutaneous;
    }

    public boolean isRectal() {
        return rectal;
    }

    public void setRectal(boolean rectal) {
        this.rectal = rectal;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<DrugField> getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(List<DrugField> brandNames) {
        this.brandNames = brandNames;
    }

    public List<DrugField> getClasses() {
        return classes;
    }

    public void setClasses(List<DrugField> classes) {
        this.classes = classes;
    }

    public List<DrugField> getIndications() {
        return indications;
    }

    public void setIndications(List<DrugField> indications) {
        this.indications = indications;
    }

    public List<DrugField> getContraindications() {
        return contraindications;
    }

    public void setContraindications(List<DrugField> contraindications) {
        this.contraindications = contraindications;
    }

    public List<DrugField> getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(List<DrugField> sideEffects) {
        this.sideEffects = sideEffects;
    }

    public List<DrugField> getConsiderations() {
        return considerations;
    }

    public void setConsiderations(List<DrugField> considerations) {
        this.considerations = considerations;
    }
}
