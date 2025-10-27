    package controller.staff;

    import dto.ContractDTO;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import service.ContractService;
    import util.di.DIContainer;

    import java.io.IOException;
    import java.util.List;

    @WebServlet("/staff")
    public class SatffServlet extends HttpServlet {

        private ContractService contractService;

        @Override
        public void init() throws ServletException {
            super.init();
            try {
                contractService = DIContainer.get(ContractService.class);
            } catch (Exception e) {
                throw new RuntimeException("❌ Khong the khoi tao ContractService", e);
            }
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            try {
                // Lấy tất cả hợp đồng, không filter theo staffId
                List<ContractDTO> contracts = contractService.getAllContracts();

                request.setAttribute("contracts", contracts);
                request.getRequestDispatcher("/staff/staff.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/error.jsp?msg=load_contracts_failed");
            }
        }
    }
