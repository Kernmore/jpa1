package apartments;

import jpa1.SimpleClient;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Run {

    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("JPA-Apartments");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: create apartment request");
                    System.out.println("2: show apartments");
                    System.out.println("3: remove apartment");
                    System.out.println("4: filter apartments");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            createApartment(sc);
                            break;
                        case "2":
                            showApartments();
                            break;
                        case "3":
                            removeApartment(sc);
                            break;
                        case "4":
                            filterApartmetns(sc);
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void createApartment(Scanner sc) {
        System.out.print("Enter apartment district: ");
        String district = sc.nextLine();
        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();
        System.out.print("Enter apartment area: ");
        String sArea = sc.nextLine();
        System.out.print("Enter apartment number of rooms: ");
        String sRoomNumber = sc.nextLine();
        System.out.print("Enter apartment price: ");
        String sPrice = sc.nextLine();

        int area = Integer.parseInt(sArea);
        int roomNumber = Integer.parseInt(sRoomNumber);
        int price = Integer.parseInt(sPrice);

        em.getTransaction().begin();
        try {
            Apartment apartment = new Apartment(district, address, area, roomNumber, price);
            em.persist(apartment);
            em.getTransaction().commit();

            System.out.println(apartment.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void showApartments() {
        Query query = em.createQuery("SELECT c FROM Apartment c", Apartment.class);
        List<Apartment> list = (List<Apartment>) query.getResultList();

        for (Apartment a : list)
            System.out.println(a);
    }

    private static void removeApartment(Scanner sc) {
        System.out.print("Enter apartment id: ");
        String sId = sc.nextLine();
        long id = Long.parseLong(sId);

        Apartment c = em.getReference(Apartment.class, id);
        if (c == null) {
            System.out.println("Apartment not found!");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(c);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void filterApartmetns(Scanner sc) {
        System.out.println("Choose filter");
        System.out.println("1: By price");
        System.out.println("2: By district");
        System.out.println("3: By area");
        System.out.println("4: By number of rooms");
        System.out.println("5: Enable all filters");
        System.out.print("-> ");

        String s = sc.nextLine();

        List<Apartment> list = null;
        String filter = "";

        switch (s) {
            case "1":
                list = priceFilter(sc);
                filter = "By Price";
                break;
            case "2":
                list = districtFilter(sc);
                filter = "By District";
                break;
            case "3":
                list = areaFilter(sc);
                filter = "By Area";
                break;
            case "4":
                list = roomNumberFilter(sc);
                filter = "By Number of Rooms";
                break;
            case "5":
                list = enableAllFilters(sc);
                filter = "Enabled all functions";
                break;
            default:
                return;
        }

        System.out.printf("\nFilter: %s: \n____________________________\n", filter);
        if (list != null) {
            for (Apartment a : list) {
                System.out.println(a);
            }
        }
        System.out.println("____________________________");

    }

    private static List<Apartment> priceFilter(Scanner sc) {
        System.out.println("Your budget: \n ");
        System.out.println("from:");
        String sMinPrice = sc.nextLine();
        System.out.println("to:");
        String sMaxPrice = sc.nextLine();

        int minPrice = Integer.parseInt(sMinPrice);
        int maxPrice = Integer.parseInt(sMaxPrice);

        List<Apartment> list = null;

        try {
            Query query = em.createQuery(
                    "SELECT x FROM Apartment x WHERE x.price >:minPrice AND x.price <:maxPrice");
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);

            list = (List<Apartment>) query.getResultList();

        } catch (NoResultException ex) {
            System.out.println("Client not found!");
            return null;
        }

        em.getTransaction().begin();
        try {

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }

        return list;

    }

    private static List<Apartment> districtFilter(Scanner sc) {
        System.out.print("Press the number of the district of apartments from the list below: \n ");
        System.out.println("1. Shevchenkiv\n 2. Frankivskiy\n 3. Center\n 4. Syhiv\n");
        String district = checkDistrict(sc);

        List<Apartment> list = null;

        try {
            Query query = em.createQuery("SELECT x FROM Apartment x WHERE x.district =:district");
            query.setParameter("district", district);

            list = (List<Apartment>) query.getResultList();

        } catch (NoResultException e) {
            System.out.println("Apartment not found!");
            return null;
        }

        em.getTransaction().begin();
        try {
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }

        return list;

    }

    private static List<Apartment> areaFilter(Scanner sc) {
        System.out.println("filter the area you want: \n ");
        System.out.println("from:");
        String sMinArea = sc.nextLine();
        System.out.println("to:");
        String sMaxArea = sc.nextLine();

        int minArea = Integer.parseInt(sMinArea);
        int maxArea = Integer.parseInt(sMaxArea);

        List<Apartment> list = null;

        try {
            Query query = em.createQuery(
                    "SELECT x FROM Apartment x WHERE x.area >:minArea AND x.area <:maxArea");
            query.setParameter("minArea", minArea);
            query.setParameter("maxArea", maxArea);

            list = (List<Apartment>) query.getResultList();

        } catch (NoResultException e) {
            System.out.println("Apartment not found!");
            return null;
        }

        em.getTransaction().begin();
        try {
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }

        return list;
    }

    private static List<Apartment> roomNumberFilter(Scanner sc) {
        System.out.println("How many rooms do you want?  ");
        String sNumber = sc.nextLine();

        int roomNumber = Integer.parseInt(sNumber);

        List<Apartment> list = null;

        try {
            Query query = em.createQuery(
                    "SELECT x FROM Apartment x WHERE x.roomNumber =:roomNumber");
            query.setParameter("roomNumber", roomNumber);

            list = (List<Apartment>) query.getResultList();

        } catch (NoResultException e) {
            System.out.println("Apartment not found!");
            return null;
        }

        em.getTransaction().begin();
        try {
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }

        return list;
    }

    private static List<Apartment> enableAllFilters(Scanner sc) {
        System.out.println("Your budget: \n ");
        System.out.println("from:");
        String sMinPrice = sc.nextLine();
        System.out.println("to:");
        String sMaxPrice = sc.nextLine();

        System.out.print("Press the number of the district of apartments from the list below: \n ");
        System.out.println("1. Shevchenkiv\n 2. Frankivskiy\n 3. Center\n 4. Syhiv\n");
        String district = checkDistrict(sc);

        System.out.println("filter the area you want: \n  ");
        System.out.println("from:");
        String sMinArea = sc.nextLine();
        System.out.println("to:");
        String sMaxArea = sc.nextLine();

        System.out.println("How many rooms do you want?  ");
        String sNumber = sc.nextLine();

        int roomNumber = Integer.parseInt(sNumber);
        int minArea = Integer.parseInt(sMinArea);
        int maxArea = Integer.parseInt(sMaxArea);
        int minPrice = Integer.parseInt(sMinPrice);
        int maxPrice = Integer.parseInt(sMaxPrice);


        List<Apartment> list = null;

        try {
            Query query = em.createQuery(
                    "SELECT x FROM Apartment x WHERE x.roomNumber =:roomNumber " +
                            "AND x.price BETWEEN :minPrice AND :maxPrice " +
                            "AND x.area BETWEEN :minArea AND :maxArea AND x.district =:district");
            query.setParameter("roomNumber", roomNumber);
            query.setParameter("maxPrice", maxPrice);
            query.setParameter("minPrice", minPrice);
            query.setParameter("minArea", minArea);
            query.setParameter("maxArea", maxArea);
            query.setParameter("district", district);

            list = (List<Apartment>) query.getResultList();

        } catch (NoResultException e) {
            System.out.println("Apartment not found!");
            return null;
        }

        em.getTransaction().begin();
        try {
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }

        return list;

    }

    private static String checkDistrict(Scanner sc){
        String s = sc.nextLine();
        int number = Integer.parseInt(s);

        switch (number){
            case 1: s = "Shevchenkiv";
            break;
            case 2: s = "Frankivskiy";
            break;
            case 3: s = "Center";
            break;
            case 4: s = "Syhiv";
            break;
            default: return null;
        }
        return s;
    }
}




