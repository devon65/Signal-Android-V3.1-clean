// Code generated by dagger-compiler.  Do not edit.
package org.thoughtcrime.securesms.preferences;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binding<AppProtectionPreferenceFragment>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code AppProtectionPreferenceFragment} and its
 * dependencies.
 *
 * Being a {@code Provider<AppProtectionPreferenceFragment>} and handling creation and
 * preparation of object instances.
 *
 * Being a {@code MembersInjector<AppProtectionPreferenceFragment>} and handling injection
 * of annotated fields.
 */
public final class AppProtectionPreferenceFragment$$InjectAdapter extends Binding<AppProtectionPreferenceFragment>
    implements Provider<AppProtectionPreferenceFragment>, MembersInjector<AppProtectionPreferenceFragment> {
  private Binding<org.whispersystems.signalservice.api.SignalServiceAccountManager> accountManager;
  private Binding<CorrectedPreferenceFragment> supertype;

  public AppProtectionPreferenceFragment$$InjectAdapter() {
    super("org.thoughtcrime.securesms.preferences.AppProtectionPreferenceFragment", "members/org.thoughtcrime.securesms.preferences.AppProtectionPreferenceFragment", NOT_SINGLETON, AppProtectionPreferenceFragment.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    accountManager = (Binding<org.whispersystems.signalservice.api.SignalServiceAccountManager>) linker.requestBinding("org.whispersystems.signalservice.api.SignalServiceAccountManager", AppProtectionPreferenceFragment.class, getClass().getClassLoader());
    supertype = (Binding<CorrectedPreferenceFragment>) linker.requestBinding("members/org.thoughtcrime.securesms.preferences.CorrectedPreferenceFragment", AppProtectionPreferenceFragment.class, getClass().getClassLoader(), false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(accountManager);
    injectMembersBindings.add(supertype);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<AppProtectionPreferenceFragment>}.
   */
  @Override
  public AppProtectionPreferenceFragment get() {
    AppProtectionPreferenceFragment result = new AppProtectionPreferenceFragment();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<AppProtectionPreferenceFragment>}.
   */
  @Override
  public void injectMembers(AppProtectionPreferenceFragment object) {
    object.accountManager = accountManager.get();
    supertype.injectMembers(object);
  }

}
