package galip.hro.inspectorsgadget;

import java.util.LinkedList;

/**
 * Created by Martijn on 8-10-2014.
 */
public class CompareLists {

    LinkedList<String> listContainersHarbour = new LinkedList<String>();
    LinkedList<String> listContainersShip = new LinkedList<String>();
    LinkedList<String> listContainersMissing = new LinkedList<String>();

    public CompareLists(LinkedList<String> listContainersHarbour, LinkedList<String> listContainersShip)
    {
        this.listContainersHarbour = listContainersHarbour;
        this.listContainersShip = listContainersShip;
    }

    public LinkedList<String> compare() {
        for (String container : listContainersShip) {
            if (listContainersHarbour.contains(container) == false)
                listContainersMissing.add(container);
        }

        return listContainersMissing;
    }
}
