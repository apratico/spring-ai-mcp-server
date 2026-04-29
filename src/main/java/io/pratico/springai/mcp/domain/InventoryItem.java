package io.pratico.springai.mcp.domain;

public record InventoryItem(
        String materialCode,
        String description,
        int quantityOnHand,
        int reorderPoint,
        String unitOfMeasure,
        String warehouseLocation
) {
    public boolean isBelowReorderPoint() {
        return quantityOnHand < reorderPoint;
    }
}
