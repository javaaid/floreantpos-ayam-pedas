package com.floreantpos.model;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseTicketItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TicketItem extends BaseTicketItem implements ITicketItem {
    private static final long serialVersionUID = 1L;

    /*[CONSTRUCTOR MARKER BEGIN]*/
    public TicketItem() {
        super();
    }

    /**
     * Constructor for primary key
     */
    public TicketItem(java.lang.Integer id) {
        super(id);
    }

    /**
     * Constructor for required fields
     */
    public TicketItem(java.lang.Integer id, com.floreantpos.model.Ticket ticket) {

        super(id, ticket);
    }

	/*[CONSTRUCTOR MARKER END]*/

    private int tableRowNum;

    public int getTableRowNum() {
        return tableRowNum;
    }

    public void setTableRowNum(int tableRowNum) {
        this.tableRowNum = tableRowNum;
    }

    public boolean canAddCookingInstruction() {
        if (isPrintedToKitchen())
            return false;

        return true;
    }

    public java.lang.Double getTaxAmount() {
        if(getTicket().isTaxExempt()) {
            return 0.0;
        }

        return super.getTaxAmount();
    }

    @Override
    public String toString() {
        return getName();
    }

    public void addCookingInstruction(TicketItemCookingInstruction cookingInstruction) {
        List<TicketItemCookingInstruction> cookingInstructions = getCookingInstructions();

        if (cookingInstructions == null) {
            cookingInstructions = new ArrayList<TicketItemCookingInstruction>(2);
            setCookingInstructions(cookingInstructions);
        }

        cookingInstructions.add(cookingInstruction);
    }

    public void addCookingInstructions(List<TicketItemCookingInstruction> instructions) {
        List<TicketItemCookingInstruction> cookingInstructions = getCookingInstructions();

        if (cookingInstructions == null) {
            cookingInstructions = new ArrayList<TicketItemCookingInstruction>(2);
            setCookingInstructions(cookingInstructions);
        }

        cookingInstructions.addAll(instructions);
    }

    public void removeCookingInstruction(TicketItemCookingInstruction itemCookingInstruction) {
        List<TicketItemCookingInstruction> cookingInstructions2 = getCookingInstructions();
        if (cookingInstructions2 == null) {
            return;
        }

        for (Iterator iterator = cookingInstructions2.iterator(); iterator.hasNext();) {
            TicketItemCookingInstruction ticketItemCookingInstruction = (TicketItemCookingInstruction) iterator.next();
            if (ticketItemCookingInstruction.getTableRowNum() == itemCookingInstruction.getTableRowNum()) {
                iterator.remove();
                return;
            }
        }
    }

    public TicketItemModifierGroup findTicketItemModifierGroup(MenuModifier menuModifier, boolean createNew) {
        MenuItemModifierGroup menuItemModifierGroup = menuModifier.getMenuItemModifierGroup();

        List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();

        if (ticketItemModifierGroups != null) {
            for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
                if (ticketItemModifierGroup.getModifierGroupId().equals(menuItemModifierGroup.getId())) {
                    return ticketItemModifierGroup;
                }
            }
        }

        TicketItemModifierGroup ticketItemModifierGroup = new TicketItemModifierGroup();
        ticketItemModifierGroup.setModifierGroupId(menuItemModifierGroup.getId());
        ticketItemModifierGroup.setMinQuantity(menuItemModifierGroup.getMinQuantity());
        ticketItemModifierGroup.setMaxQuantity(menuItemModifierGroup.getMaxQuantity());
        ticketItemModifierGroup.setParent(this);
        addToticketItemModifierGroups(ticketItemModifierGroup);

        return ticketItemModifierGroup;
    }

    public void calculatePrice() {



        List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
        if (ticketItemModifierGroups != null) {
            for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
                ticketItemModifierGroup.calculatePrice();
            }
        }

        setSubtotalAmount((calculateSubtotal(true)));
        setSubtotalAmountWithoutModifiers((calculateSubtotal(false)));
        setDiscountAmount((calculateDiscount()));
        setTaxAmount((calculateTax(true)));
        setTaxAmountWithoutModifiers((calculateTax(false)));
        setTotalAmount((calculateTotal(true)));
        setTotalAmountWithoutModifiers((calculateTotal(false)));
    }

    //	public double calculateSubtotal() {
    //		double subtotal = NumberUtil.roundToTwoDigit(calculateSubtotal(true));
    //
    //		return subtotal;
    //	}
    //
    //	public double calculateSubtotalWithoutModifiers() {
    //		double subtotalWithoutModifiers = NumberUtil.roundToTwoDigit(calculateSubtotal(false));
    //
    //		return subtotalWithoutModifiers;
    //	}

    private double calculateSubtotal(boolean includeModifierPrice) {
        double subTotalAmount = (getUnitPrice() * getItemCount());

        if (includeModifierPrice) {
            List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
            if (ticketItemModifierGroups != null) {
                for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
                    subTotalAmount += ticketItemModifierGroup.getSubtotal();
                }
            }
        }

        return subTotalAmount;
    }

    private double calculateDiscount() {
        double subtotalWithoutModifiers = getSubtotalAmountWithoutModifiers();
        double discountRate = getDiscountRate();

        double discount = 0;
        if (discountRate > 0) {
            discount = subtotalWithoutModifiers * discountRate / 100.0;
        }

        return discount;
    }

    private double calculateTax(boolean includeModifierTax) {
        double subtotal;

        subtotal = getSubtotalAmountWithoutModifiers();

        double discount = getDiscountAmount();

        subtotal = subtotal - discount;

        double taxRate = getTaxRate();
        double tax = 0;

        if (taxRate > 0) {
            if (Application.getInstance().isPriceIncludesTax()) {
                tax = subtotal - (subtotal / (1 + (taxRate / 100.0)));
            }
            else {
                tax = subtotal * (taxRate / 100.0);
            }
        }

        if (includeModifierTax) {
            List<TicketItemModifierGroup> ticketItemModifierGroups = getTicketItemModifierGroups();
            if (ticketItemModifierGroups != null) {
                for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
                    tax += ticketItemModifierGroup.getTax();
                }
            }
        }

        return tax;
    }

    private double calculateTotal(boolean includeModifiers) {
        double total;

        if (includeModifiers) {
            if (Application.getInstance().isPriceIncludesTax()) {
                total = getSubtotalAmount() - getDiscountAmount();
            }
            else {
//				total = getSubtotalAmount() - getDiscountAmount() + getTaxAmount();
                total = getSubtotalAmount() - getDiscountAmount(); //TODO
            }
        }
        else {
            if (Application.getInstance().isPriceIncludesTax()) {
                total = getSubtotalAmountWithoutModifiers() - getDiscountAmount();
            }
            else {
//				total = getSubtotalAmountWithoutModifiers() - getDiscountAmount() + getTaxAmountWithoutModifiers();
                total = getSubtotalAmountWithoutModifiers() - getDiscountAmount(); //TODO
            }
        }

        return total;
    }

    @Override
    public String getNameDisplay() {
        return getName();
    }

    @Override
    public Double getUnitPriceDisplay() {
        return getUnitPrice();
    }

    @Override
    public Integer getItemCountDisplay() {
        return getItemCount();
    }

    @Override
    public Double getTaxAmountWithoutModifiersDisplay() {
        return getTaxAmountWithoutModifiers();
    }

    @Override
    public Double getTotalAmountWithoutModifiersDisplay() {
        return getTotalAmountWithoutModifiers();
    }

}